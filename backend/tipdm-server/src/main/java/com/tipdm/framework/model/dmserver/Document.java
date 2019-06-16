package com.tipdm.framework.model.dmserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipdm.framework.model.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by TipDM on 2016/12/9.
 * E-mail:devp@tipdm.com
 * 文档
 */
@Entity
@Table(name = "dm_document", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "parent_id", "creator_id"})})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Document extends IdEntity<Long> {

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "is_leaf", nullable = false)
    private Boolean isLeaf = Boolean.FALSE;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = Boolean.FALSE;

    @Column(name = "parent_id")
    private Long parentId = 0L;

    @Column
    private String path = "/";

    @OneToOne(targetEntity = Project.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", unique = true)
    private Project project;

    @Column(name = "update_time")
    private Date updateTime;//修改时间

    @Column(name = "sequence")
    private Integer sequence;//排序ID

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
