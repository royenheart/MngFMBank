package com.royenheart.server.databaseopt;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 数据库操作，Delete类型操作
 * @author RoyenHeart
 */
abstract public class DatabaseDelete extends DatabaseOperations {

    protected String tables;
    protected HashMap<String, String> keyValue;

    public DatabaseDelete() {}

    protected String getSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM " + tables + " where ");
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
