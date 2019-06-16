package com.tipdm.framework.model.dmserver;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by TipDM on 2017/3/16.
 * E-mail:devp@tipdm.com
 */
@Entity
@Table(name = "dm_audience")
public class Audience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "shared_object_id")
    private Long sharedObjectId;

    @Column(name = "object_type")
    @Enumerated()
    private ShareType objectType;

    public Audience(){

    }

    public Audience(Long userId, String userName, Long sharedObjectId){
        this.userId = userId;
        this.userName = userName;
        this.sharedObjectId = sharedObjectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getSharedObjectId() {
        return sharedObjectId;
    }

    public void setSharedObjectId(Long sharedObjectId) {
        this.sharedObjectId = sharedObjectId;
    }

    public ShareType getObjectType() {
        return objectType;
    }

    public void setObjectType(ShareType objectType) {
        this.objectType = objectType;
    }
}
