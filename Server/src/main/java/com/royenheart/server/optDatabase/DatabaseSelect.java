package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 数据库操作，Select操作
 * @author RoyenHeart
 */
abstract public class DatabaseSelect extends DatabaseOperations {
    protected String database;
    /*判断条件*/
    protected HashMap<String, String> keyValue;
    /*所需的信息*/
    protected LinkedList<String> fields;

    public DatabaseSelect(Connection con, String database) {
        super(con);
        this.database = database;
    }

    public void addKeyValue(String key, String value) {
        keyValue.put(key, value);
    }

    public void addFields(String key) {
        fields.add(key);
    }

    protected String getSql() {
        StringBuffer sql = new StringBuffer("SELECT ");

        Iterator<String> iteratorW = fields.iterator();
        while (iteratorW.hasNext()) {
            sql.append(iteratorW.next());
            if (iteratorW.hasNext()) {
                sql.append(",");
            }
        }

        sql.append(" FROM ").append(database).append(" where ");

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
