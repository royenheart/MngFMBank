package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 数据库操作，Delete类型操作
 * @author RoyenHeart
 */
abstract public class DatabaseDelete extends DatabaseOperations {

    protected String database;
    protected HashMap<String, String> keyValue;

    public DatabaseDelete(Connection con, String database) {
        super(con);
        this.database = database;
    }

    public void addKeyValue(String key, String value) {
        keyValue.put(key, value);
    }

    protected String getSql() {
        StringBuffer sql = new StringBuffer("DELETE FROM " + database + " where ");
        Iterator<String> iterator = keyValue.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = keyValue.get(key);
            sql.append(String.format("%s=\"%s\"", key, value));
            if (iterator.hasNext()) {
                sql.append(" AND ");
            } else {
                sql.append(" ");
            }
        }
        sql.append(";");
        return sql.toString();
    }

}
