package com.dreamer.wanderer.data.hibernate.beans;

import com.dreamer.wanderer.data.hibernate.HibernateBean;

/**
 * Created by rjain236 on 26/7/15.
 */
public class SchemaBasedBean extends HibernateBean{

    private String clazz;
    private String keyword;
    private byte[] schemaData;

    public SchemaBasedBean(String clazz, String keyword, byte[] schemaData) {
        this.clazz = clazz;
        this.keyword = keyword;
        this.schemaData = schemaData;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public byte[] getSchemaData() {
        return schemaData;
    }

    public void setSchemaData(byte[] schemaData) {
        this.schemaData = schemaData;
    }
}
