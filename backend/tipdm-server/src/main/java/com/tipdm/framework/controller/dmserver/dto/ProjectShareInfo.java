package com.tipdm.framework.controller.dmserver.dto;

/**
 * Created by TipDM on 2017/8/13.
 * E-mail:devp@tipdm.com
 */
public class ProjectShareInfo {

    private String owner;

    private Long projectId;

    public ProjectShareInfo(){

    }

    public ProjectShareInfo(String owner, Long projectId){
        this.owner = owner;
        this.projectId = projectId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
