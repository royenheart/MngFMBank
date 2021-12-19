package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 数据库操作，Update类型操作
 * @author RoyenHeart
 */
abstract public class DataBaseUpdate extends DatabaseOperations {
    protected String database;
    protected HashMap<String, String> keyValue;
    protected String field;
    protected String fieldValue;

    public DataBaseUpdate(Connection con, String database, String field, String fieldValue) {
        super(con);
        this.database = database;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    public void addKeyValue(String key, String value) {
        keyValue.put(key, value);
    }

    protected String getSql() {
        StringBuffer sql = new StringBuffer("UPDATE " + database + " SET ");

        sql.append(field).append("=").append(fieldValue).append(" where ");

        Iterator<String> iteratorF = keyValue.keySet().iterator();
        while (iteratorF.hasNext()) {
            String key = iteratorF.next();
            String value = keyValue.get(key);
            sql.append(String.format("%s=\"%s\"", key, value));
            if (iteratorF.hasNext()) {
                sql.append(" AND ");
            } else {
                sql.append(" ");
            }
        }
        sql.append(";");
        return sql.toString();
    }
}
