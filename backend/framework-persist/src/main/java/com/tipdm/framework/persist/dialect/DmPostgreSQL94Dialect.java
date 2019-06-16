package com.tipdm.framework.persist.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

/**
 * Created by zhoulong on 2016/12/10.
 * E-mail:zhoulong8513@gmail.com
 */
public class DmPostgreSQL94Dialect extends PostgreSQL94Dialect {

    public DmPostgreSQL94Dialect(){
        super();

        this.registerColumnType(Types.JAVA_OBJECT, "json");
        this.registerColumnType(Types.LONGNVARCHAR, "text");
        this.registerHibernateType(Types.LONGVARCHAR, "string");
    }
}
