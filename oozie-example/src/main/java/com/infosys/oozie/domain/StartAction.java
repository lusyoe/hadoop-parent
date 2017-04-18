package com.infosys.oozie.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class StartAction {

    @JacksonXmlProperty(localName = "to", isAttribute = true)
    private String to;

    public String getTo() {

        return to;
    }

    public void setTo(String to) {

        this.to = to;
    }

}
