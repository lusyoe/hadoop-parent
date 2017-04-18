package com.infosys.oozie.domain;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "workflow-app")
public class OozieWorkFlowModel implements Serializable {

    private static final long serialVersionUID = -90421270790305806L;

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String workflowApp;

    @JacksonXmlProperty(localName = "start")
    private StartAction start;

    @JacksonXmlProperty(localName = "action")
    private Action action;

    @JacksonXmlProperty(localName = "sqoop")
    private SqoopAction sqoop;

    public String getWorkflowApp() {

        return workflowApp;
    }

    public void setWorkflowApp(String workflowApp) {

        this.workflowApp = workflowApp;
    }

    public StartAction getStart() {

        return start;
    }

    public void setStart(StartAction start) {

        this.start = start;
    }

    public Action getAction() {

        return action;
    }

    public void setAction(Action action) {

        this.action = action;
    }

    public SqoopAction getSqoop() {

        return sqoop;
    }

    public void setSqoop(SqoopAction sqoop) {

        this.sqoop = sqoop;
    }

}
