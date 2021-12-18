package com.royenheart.basicsets.jsonsettings;

import java.io.File;

/**
 * Json文件写入
 * @author RoyenHeart
 */
abstract public class JsonWriter {

    private boolean mkdirResources(File make) throws SecurityException {
        return make.mkdir();
    }

    /**
     * 序列化对象并并写入对应的Json文件
     */
    abstract void store();

}
