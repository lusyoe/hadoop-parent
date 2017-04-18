package com.infosys.oozie.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.infosys.oozie.domain.Action;
import com.infosys.oozie.domain.OozieWorkFlowModel;
import com.infosys.oozie.domain.SqoopAction;
import com.infosys.oozie.domain.StartAction;

@RestController
public class XmlController {

    @Value("${spring.datasource.url}")
    private String dbConnUrl;
    
    @Value("${oozie.job.tracker}")
    private String jobTracker;
    
    
    @RequestMapping(value = "/wf", produces = { MediaType.APPLICATION_XML_VALUE })
    public OozieWorkFlowModel getWorkFlow() throws Exception {

        OozieWorkFlowModel wf = new OozieWorkFlowModel();
        StartAction startAction = new StartAction();
        Action action = new Action();
        SqoopAction sqoopAction = new SqoopAction();

        startAction.setTo("mysqoopAction");
        wf.setWorkflowApp("my-Wf");

        wf.setStart(startAction);

        action.setName("mysqoopAction");
        wf.setAction(action);

        sqoopAction.setJobTracker(jobTracker);
        sqoopAction.setNameNode("e5:8020");
        
        List<String> args = sqoopAction.getArg();
        args.add("import");
        args.add("--connect");
        args.add(dbConnUrl);
        args.add("--username");
        

        wf.setSqoop(sqoopAction);

        XmlMapper xmlMapper = new XmlMapper();

        String wfPath = this.getClass().getResource("/").getPath();
        
        File workflow = new File(wfPath, "/workflow.xml");
        
        if (!workflow.exists()) {
            workflow.createNewFile();
        } else {
            workflow.delete();
        }
        
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(workflow, wf);
        
        return wf;
    }
}
