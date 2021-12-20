package com.royenheart.basicsets.jsonsettings;

/**
 * Json文件写入
 * @author RoyenHeart
 */
abstract public class JsonWriter {

    /**
     * 序列化对象并并写入对应的Json文件
     * @param obj 存储为Json文本形式的对象
     * @return 是否存储成功
     */
    abstract boolean store(Object obj);

}
