package com.royenheart.basicsets.jsonsettings;

import java.io.File;

/**
 * Json文件读入，抽象类
 * @author RoyenHeart
 */
abstract public class JsonReader {

    private boolean mkdirResources(File make) throws SecurityException {
        return make.mkdir();
    }

    /**
     * Json文件读入并返回对应对象
     */
    abstract void initial();

}
