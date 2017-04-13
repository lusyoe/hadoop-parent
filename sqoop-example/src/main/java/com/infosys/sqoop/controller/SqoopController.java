package com.infosys.sqoop.controller;

import static com.infosys.hadoop.common.utils.Constants.DIALECT;
import static com.infosys.hadoop.common.utils.Constants.MYSQL_DRIVER_STR;
import static com.infosys.hadoop.common.utils.Constants.SQOOP2_GENERIC_CONNECTOR;
import static com.infosys.hadoop.common.utils.Constants.SQOOP2_HDFS_CONNECTOR;

import java.util.List;

import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.client.SubmissionCallback;
import org.apache.sqoop.model.MFromConfig;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MLink;
import org.apache.sqoop.model.MLinkConfig;
import org.apache.sqoop.model.MSubmission;
import org.apache.sqoop.model.MToConfig;
import org.apache.sqoop.validation.Status;
import org.hadoop.domain.FromJobConfig;
import org.hadoop.domain.LinkConfig;
import org.hadoop.domain.ToFormat;
import org.hadoop.domain.ToJobConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sqoop")
public class SqoopController {

    private static final Logger log = LoggerFactory.getLogger(SqoopController.class);

    // 真实情况通过数据库读取以下配置
    private static final String DB_SCHEMA = "jdbc:mysql://192.168.1.2:3306/hadoopguide?useSSL=false";
    private static final String JOB_NAME = "web-job";
    private static final String SOURCE_DB = "hadoopguide";
    private static final String SOURCE_TABLE = "widgets";
    private static final String HDFS_OUTPUT = "/sqoop/out";
    
    
    @Value("${spring.datasource.username}")
    private String DB_USERNAME;

    @Value("${spring.datasource.password}")
    private String DB_PASSWD;
    
    @Value("${sqoop.url}")
    private String sqoopUrl;

    @Value("${sqoop.from.linkname}")
    private String fromLinkName;

    @Value("${sqoop.to.linkname}")
    private String toLinkName;

    @Value("${hadoop.conf.dir}")
    private String hadoopConfDir;
    
    

    @RequestMapping(value = "/startJob", method = RequestMethod.GET)
    public String startJob() throws Exception {

        SqoopClient client = new SqoopClient(sqoopUrl);

        configSourceLink(client);

        configDestLink(client);
        
        configJob(client);

        startJob(client);
        
        return "SUCCESS";
    }

    @RequestMapping(value = "/delAll", method = RequestMethod.GET)
    public String delAllLinksAndJobs() throws Exception {

        SqoopClient client = new SqoopClient(sqoopUrl);
        
        List<MJob> jobs = client.getJobs();

        for (MJob job : jobs) {
            
            String jobName = job.getName();
            MSubmission jobStatus = client.getJobStatus(jobName);
            
            if (jobStatus.getStatus().isRunning()) {
                client.stopJob(jobName);
            }
        }
        
        client.deleteAllLinksAndJobs();
        
        return "SUCCESS";
    }
    
    
    /**
     * 启动Job
     * @param client
     * @throws InterruptedException 
     */
    private void startJob(SqoopClient client) throws InterruptedException {
        
        // 启动Job并设置回调
        MSubmission submission = client.startJob(JOB_NAME, DEFAULT_SUBMISSION_CALLBACKS, 100);
        
        if (submission.getStatus().isFailure()) {
            log.error("Submission has failed:" + submission.getError().getErrorSummary());
            log.error("Corresponding error details: " + submission.getError().getErrorDetails());
        }
    }
    
    protected static final SubmissionCallback DEFAULT_SUBMISSION_CALLBACKS = new SubmissionCallback() {
        @Override
        public void submitted(MSubmission submission) {
            log.info("Submission submitted: " + submission);
        }

        @Override
        public void updated(MSubmission submission) {
            log.info("Submission updated: " + submission);
        }

        @Override
        public void finished(MSubmission submission) {
            log.info("Submission finished: " + submission);
        }
    };


    /**
     * 配置Job
     * @param client
     */
    private void configJob(SqoopClient client) {
        
        MJob job = client.createJob(fromLinkName, toLinkName);
        job.setName(JOB_NAME);
        
        MFromConfig fromJobConfig = job.getFromJobConfig();
        
        // 配置源数据库
        fromJobConfig.getStringInput(FromJobConfig.SCHEMA_NAME).setValue(SOURCE_DB);
        
        fromJobConfig.getStringInput(FromJobConfig.TABLE_NAME).setValue(SOURCE_TABLE);
        
        fromJobConfig.getStringInput(FromJobConfig.PARITITION_COLUMN).setValue("id");
        
        // 配置目标Job
        MToConfig toJobConfig = job.getToJobConfig();
        
        toJobConfig.getStringInput(ToJobConfig.OUT_DIR).setValue(HDFS_OUTPUT);
        
        toJobConfig.getBooleanInput(ToJobConfig.APPEND_MODE).setValue(true);
        
        toJobConfig.getEnumInput(ToJobConfig.OUT_FORMAT).setValue(ToFormat.TEXT_FILE);
        
        client.saveJob(job);
    }

    
    /**
     * 配置目标链接
     * @param client
     */
    private void configDestLink(SqoopClient client) {

        MLink destLink = client.createLink(SQOOP2_HDFS_CONNECTOR);
        destLink.setName(toLinkName);

        MLinkConfig destLinkConfig = destLink.getConnectorLinkConfig();

        destLinkConfig.getStringInput(LinkConfig.CONF_DIR).setValue(hadoopConfDir);

        Status status = client.saveLink(destLink);

        if (status.canProceed()) {
            log.debug("Created Dest Link with Link Name : " + destLink.getName());
        } else {
            log.debug("Something went wrong creating the Dest link");
        }
    }
    
    

    /**
     * 配置源链接
     * @param client
     */
    private void configSourceLink(SqoopClient client) {

        MLink link = client.createLink(SQOOP2_GENERIC_CONNECTOR);

        link.setName(fromLinkName);

        MLinkConfig linkConfig = link.getConnectorLinkConfig();

        // 配置源link属性
        linkConfig.getStringInput(LinkConfig.CONN_STRING).setValue(DB_SCHEMA);

        // 配置MySQL数据库驱动
        linkConfig.getStringInput(LinkConfig.JDBC_DRIVER).setValue(MYSQL_DRIVER_STR);

        // 配置数据链接用户和密码
        linkConfig.getStringInput(LinkConfig.USERNAME).setValue(DB_USERNAME);
        linkConfig.getStringInput(LinkConfig.PASSWORD).setValue(DB_PASSWD);

        // mysql这里地方必须要注意了，如果不设置会报SQL语法错误，
        // 因为默认是使用"包裹数据库和表名的，这个mysql不支持
        linkConfig.getStringInput(DIALECT).setValue("");

        log.debug("source link conf = " + linkConfig.toString());

        Status status = client.saveLink(link);

        if (status.canProceed()) {
            log.debug("Created Link with Link Name : " + link.getName());
        } else {
            log.debug("Something went wrong creating the link");
        }

    }

}
