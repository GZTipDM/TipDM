package com.tipdm.framework.model.dmserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipdm.framework.model.IdEntity;
import com.tipdm.framework.persist.datatype.JsonType;
import com.tipdm.framework.persist.datatype.TextType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TipDM on 2017/3/27.
 * E-mail:devp@tipdm.com
 * 工程模板
 */
@Entity
@Table(name = "dm_template")
@TypeDefs( {@TypeDef( name= "json", typeClass = JsonType.class), @TypeDef( name= "text", typeClass = TextType.class)})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Template extends IdEntity<Long> {

    @Column
    @Type(type = "text")
    private String tags;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime = Calendar.getInstance().getTime();

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;//关联的工程

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
