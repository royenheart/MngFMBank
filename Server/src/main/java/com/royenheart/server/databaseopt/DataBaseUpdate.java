package com.royenheart.server.databaseopt;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 数据库操作，Update类型操作
 * @author RoyenHeart
 */
abstract public class DataBaseUpdate extends DatabaseOperations {
    protected String tables;
    protected HashMap<String, String> keyValue;
    protected HashMap<String, String> fieldWithValue;

    public DataBaseUpdate() {}

    protected String getSql() {
        StringBuilder sql = new StringBuilder("UPDATE " + tables + " SET ");

        /*
        获取更新的数值的键值对填入sql语句中
         */
        Iterator<String> fields = fieldWithValue.keySet().iterator();
        while (fields.hasNext()) {
            String field = fields.next();
            String value = fieldWithValue.get(field);
            sql.append(field).append("=").append("'").append(value).append("'");
            if (fields.hasNext()) {
                sql.append(",");
            }
        }
        sql.append(" WHERE ");

        /*
        获取sql条件子句
         */
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
