package com.talentflow.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public class ResumeStorageProperties {
    private String resumeDirectory;

    public String getResumeDirectory() {
        return resumeDirectory;
    }

    public void setResumeDirectory(String resumeDirectory) {
        this.resumeDirectory = resumeDirectory;
    }
}
