package com.royenheart.server.databaseopt;

import java.sql.ResultSet;

/**
 * 数据库操作返回数据约束
 * @author RoyenHeart
 */
public interface DataLimit {

    /**
     * 根据数据库查询的数据返回对应的数据
     * @param rs 从数据库查询的数据
     * @return 返回Json格式数据
     */
    String returnData(ResultSet rs);

}
