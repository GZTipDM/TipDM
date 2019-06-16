package com.tipdm.framework.dmserver.core.scheduling.model;

import com.tipdm.framework.common.utils.PropertiesUtil;
import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.core.scheduling.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Created by TipDM on 2017/1/7.
 * E-mail:devp@tipdm.com
 */
public class Job implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(Job.class);

    private static final long serialVersionUID = -2673002853824281869L;

    private static Long expiredSeconds;

    static {
        expiredSeconds = Long.parseLong(PropertiesUtil.getValue("sysconfig/redis.properties", "redis.tableExpired.seconds", "604800"));
        logger.info("redis.keyExpiredSeconds:{}", expiredSeconds);
    }

    private String jobId; // 任务 ID

    private String jobName; // 任务名称

    private String jobGroup; // 任务分组

    private String cronExpression; // 时间表达式

    private String description; // 任务描述

    private boolean endedNode = Boolean.FALSE;

    private String targetClazz; // Spring 注入的类名

    private State state;

    private Map<String, Object> attachment = new HashMap<>();

    public Job(){

    }

    public Job(String jobId, String jobName, String jobGroup){
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
    }

    public String getJobId() {
        return jobId;
    }

    public String getFullJobId() {
        return this.jobGroup + "." + jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetClazz() {
        if(StringKit.isNotBlank(targetClazz)){
            if(!targetClazz.startsWith("com.tipdm.framework.dmserver.core")){
                return StringKit.replace(targetClazz, "com.tipdm.framework.dmserver", "com.tipdm.framework.dmserver.core");
            }
        }
        return targetClazz;
    }

    public void setTargetClazz(String targetClazz) {
        this.targetClazz = targetClazz;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }


    public void setEndedNode(boolean endedNode) {
        this.endedNode = endedNode;
    }

    public boolean isEndedNode(){
        return endedNode;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
