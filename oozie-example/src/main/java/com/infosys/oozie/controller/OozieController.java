package com.infosys.oozie.controller;

import java.util.Properties;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oozie")
public class OozieController {

    private static final Logger log = LoggerFactory.getLogger(OozieController.class);

    private static final int DEBUG_ON = 1;

    @Value("${oozie.url}")
    private String oozieUrl;

     @Value("${oozie.app.path}")
     private String appPath;

    @Value("${oozie.job.tracker}")
    private String jobTracker;

    @Value("${oozie.input.dir}")
    private String inputDir;

    @Value("${oozie.output.dir}")
    private String outputDir;

    @Value("${hadoop.url}")
    private String hdfsUrl;

//    @Value("${oozie.queue.name}")
//    private String queueName;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${oozie.coord.start}")
    private String startTime;

    @Value("${oozie.coord.end}")
    private String endTime;

    @RequestMapping(value = "/initStart")
    public String initStartJob() throws OozieClientException, InterruptedException {

        OozieClient oozieClient = new OozieClient(oozieUrl);

        String user = "lu";
        // 开启debug
        oozieClient.setDebugMode(DEBUG_ON);

        Properties conf = oozieClient.createConfiguration();
        conf.setProperty(OozieClient.USER_NAME, user);
        conf.setProperty(OozieClient.APP_PATH, hdfsUrl + "/user/" + user + "/oozie/apps");

        // 设置workflow 参数
        conf.setProperty("jobTracker", jobTracker);
        conf.setProperty("nameNode", hdfsUrl);
//        conf.setProperty("queueName", queueName);

        // conf.setProperty("oozie.libpath", oozieLibPath);
        // conf.setProperty("oozie.wf.rerun.failnodes", "true");
        // conf.setProperty("inputDir", inputDir);

        conf.setProperty("dbUserName", dbUserName);
        conf.setProperty("dbPassword", dbPassword);

        conf.setProperty(OozieClient.USE_SYSTEM_LIBPATH, "true");
        conf.setProperty("outputDir", outputDir);

        // 提交运行job
        String jobId = oozieClient.run(conf);
        WorkflowJob jobInfo = oozieClient.getJobInfo(jobId);

        // while (jobInfo.getStatus() == Status.RUNNING) {
        // log.debug("Workflow job running ...");
        // Thread.sleep(10 * 1000);
        // }

        log.debug("Workflow job completed ..." + jobId);

        return "SUCCESS";
    }

    @GetMapping(value = "/killJob/{jobId}")
    public String killJobByid(@PathVariable String jobId) throws OozieClientException {

        OozieClient oozieClient = new OozieClient(oozieUrl);

        oozieClient.kill(jobId);

        return "SUCCESS";
    }
    
    @GetMapping(value = "/startCoord")
    public String startCoord() throws OozieClientException {
        OozieClient oozieClient = new OozieClient(oozieUrl);
        oozieClient.setDebugMode(DEBUG_ON);
        
        Properties conf = oozieClient.createConfiguration();
        
        conf.setProperty("jobTracker", jobTracker);
        conf.setProperty("nameNode", hdfsUrl);
        
        conf.setProperty("dbUserName", dbUserName);
        conf.setProperty("dbPassword", dbPassword);
        
        conf.setProperty("outDir", outputDir);
        conf.setProperty(OozieClient.COORDINATOR_APP_PATH, appPath);
        conf.setProperty("workflowApp", appPath);
//        conf.setProperty(OozieClient.APP_PATH, appPath);
        
        conf.setProperty("start", startTime);
        conf.setProperty("end", endTime);
        
        
        oozieClient.run(conf);
        
        return "SUCCESS";
    }
}
