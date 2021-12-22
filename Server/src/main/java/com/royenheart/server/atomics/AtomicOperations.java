package com.royenheart.server.atomics;

import com.royenheart.server.databaseopt.*;

import java.util.HashMap;

abstract public class AtomicOperations {

    /**
     * 数据库操作列表
     */
    protected static final HashMap<String, DatabaseOperations> OPERATIONS = new HashMap<>();

    /*
      批量添加原子操作
    */
    static {
        OPERATIONS.put("cu", new DatabaseCreateUser());
        OPERATIONS.put("du", new DatabaseDelUser());
        OPERATIONS.put("mu", new DatabaseMoneyUpdate());
        OPERATIONS.put("q", new DatabaseQuery());
        OPERATIONS.put("uu", new DatabaseUpdateUser());
    }

}
