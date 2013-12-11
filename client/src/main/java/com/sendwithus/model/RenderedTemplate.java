package com.sendwithus.model;


public class RenderedTemplate {
    public String subject; // must match JSON response
    public String html; // must match JSON response
    public String text; // must match JSON respons
    public Template template;

    public class Template {
        public String name;
        public String version_name;
    }

    public String toString() {
        return String.format("RenderedTemplate[%s]", this.template.name);
    }
}
