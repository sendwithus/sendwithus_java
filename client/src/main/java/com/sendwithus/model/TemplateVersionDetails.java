package com.sendwithus.model;

public class TemplateVersionDetails extends TemplateVersion {
    private boolean published;
    private String html;
    private String text;
    private String subject;
    private String preheader;
    private String amp_html;

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPreheader() {
        return preheader;
    }

    public void setPreheader(String preheader) {
        this.preheader = preheader;
    }

    public String getAmpHtml() {
        return amp_html;
    }

    public void setAmpHtml(String ampHtml) {
        this.amp_html = ampHtml;
    }
}
