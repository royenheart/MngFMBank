package com.royenheart.server.optDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 查询操作，在KeyValue中填入需要查询的条件，Fields为需要被查询的字段
 * @author RoyenHeart
 */
public class DatabaseQuery extends DatabaseSelect implements DataLimit {

    public DatabaseQuery() {}

    synchronized public String executeSql(Connection con, String tables, LinkedList<String> fields,
                                          HashMap<String, String> keyValue) throws SQLException {
        this.con = con;
        this.tables = tables;
        this.fields = fields;
        this.keyValue = keyValue;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(getSql());
        String data = returnData(rs);
        stmt.close();
        return data;
    }

    /**
     * 将数据库查询数据以Json格式返回
     * @param rs 数据库查询
     * @return 查询数据
     */
    @Override
    public String returnData(ResultSet rs) {
        String result;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .create();

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnNumber = metaData.getColumnCount();
            LinkedList<HashMap<String, String>> obj = new LinkedList<>();
            while (rs.next()) {
                HashMap<String, String> record = new HashMap<>();
                obj.add(record);

                for (int i = 1; i <= columnNumber; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value =  rs.getString(columnName);
                    record.put(columnName, value);
                }
            }
            result = gson.toJson(obj);
            rs.close();
        } catch (SQLException e) {
            System.err.println("发生错误，数据库连接异常" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
