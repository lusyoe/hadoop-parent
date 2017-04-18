package com.infosys.oozie.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SqoopAction {

    @JacksonXmlProperty(localName = "job-tracker")
    private String jobTracker;

    @JacksonXmlProperty(localName = "name-node")
    private String nameNode;

    @JacksonXmlElementWrapper(localName = "arg", useWrapping = false)
    private List<String> arg;

    
    
    public SqoopAction() {
        this.arg = new ArrayList<>();
    }

    public String getJobTracker() {

        return jobTracker;
    }

    public void setJobTracker(String jobTracker) {

        this.jobTracker = jobTracker;
    }

    public String getNameNode() {

        return nameNode;
    }

    public void setNameNode(String nameNode) {

        this.nameNode = nameNode;
    }

    public List<String> getArg() {

        return arg;
    }

    public void setArg(List<String> arg) {

        this.arg = arg;
    }

}
