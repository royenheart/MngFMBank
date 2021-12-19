package com.royenheart.basicsets.jsonsettings;

import java.io.File;

/**
 * Json文件写入
 * @author RoyenHeart
 */
abstract public class JsonWriter {

    /**
     * 序列化对象并并写入对应的Json文件
     */
    abstract boolean store(Object obj);

}
