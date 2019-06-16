package com.tipdm.framework.model.dmserver;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by TipDM on 2018/8/9.
 */
@Entity
@Table(name = "dm_data_schema", uniqueConstraints = {@UniqueConstraint(columnNames = {"schema_name"})})
public class DataSchema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schema_name", nullable = false)
    private String name;

    @Column(name = "schema_pwd", length = 8, nullable = false)
    private String password;

    public DataSchema() {

    }

    public DataSchema(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
