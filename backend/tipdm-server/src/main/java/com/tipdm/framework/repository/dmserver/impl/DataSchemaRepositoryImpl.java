package com.tipdm.framework.repository.dmserver.impl;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by TipDM on 2016/12/15.
 * E-mail:devp@tipdm.com
 */
public class DataSchemaRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void createSchema(String schema, String password) {

        StringBuilder sb = new StringBuilder("CREATE USER ").append(schema).append(" LOGIN PASSWORD '").append(password).append("';");
        sb.append("CREATE SCHEMA ").append(schema).append(";");
        sb.append("GRANT ").append(schema).append(" to postgres;");
        sb.append("ALTER SCHEMA ").append(schema).append(" OWNER TO ").append(schema).append(";");
        sb.append("REVOKE ").append(schema).append(" FROM postgres;");
        Query query = em.createNativeQuery(sb.toString());
        query.executeUpdate();
    }
}
