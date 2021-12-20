package com.royenheart.basicsets.jsonsettings;

import java.io.IOException;

/**
 * Json文件读入，抽象类
 * @author RoyenHeart
 */
abstract public class JsonReader {

    /**
     * Json文件读入并返回对应对象
     * @return 读入并生成的对象
     * @throws IOException 文件读入IO错误
     */
    public abstract boolean initial() throws IOException;

}
