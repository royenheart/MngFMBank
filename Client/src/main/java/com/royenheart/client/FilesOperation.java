package com.royenheart.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用字节数组列表字符串进行文件的接收和转发
 * @author RoyenHeart
 */
public class FilesOperation {

    private static final String REG_BYTES = "\\[([-]{0,1}[0-9]+,{0,1})+\\]";

    /**
     * 获取字符串中所有的字节数组子串
     * @param content 需要查询的字符串
     * @return 返回所有字节数组子串的链表集合
     */
    public LinkedList<String> getRegMulBytes(String content) {
        Pattern r = Pattern.compile(REG_BYTES, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(content);
        LinkedList<String> bytes = new LinkedList<>();

        int mStart = 0;
        while (m.find(mStart)) {
            mStart = m.end();
            bytes.add(m.group());
        }

        if (mStart == 0) {
            System.err.println("空文件");
            return null;
        } else {
            return bytes;
        }
    }

    /**
     * 根据获取到的字节数组列表字符串生成接收文件
     * @param file 需要接收的文件
     * @param response 获取的字节数组列表字符串
     * @throws IOException 文件接收错误
     */
    public void readFile(File file, String response) throws IOException {
        try (FileOutputStream excelStream = new FileOutputStream(file);
             DataOutputStream out = new DataOutputStream(excelStream)) {
            Gson gson = new Gson();

            LinkedList<String> fileBytes = getRegMulBytes(response);
            if (fileBytes == null) {
                System.err.println("空文件");
                throw new FileNotFoundException("空文件");
            }

            for (String bytes : fileBytes) {
                byte[] temp = gson.fromJson(bytes, new TypeToken<byte[]>() {}.getType());
                out.write(temp);
            }
        } catch (FileNotFoundException e) {
            System.err.println("创建文件未找到");
            throw e;
        } catch (IOException e) {
            System.err.println("文件接收失败");
            throw e;
        }
    }

    /**
     * 将给定的文件以字节数组列表字符串的形式表示出来
     * @param file 给定文件
     * @return 文件对应的字节数组列表
     */
    public String writeFile(File file) {
        byte[] data = new byte[300];
        StringBuilder response = new StringBuilder();
        Gson gson = new Gson();

        try (FileInputStream inputStream = new FileInputStream(file);
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            while (dataInputStream.read(data) > 0) {
                String js = gson.toJson(data).replace(" ", "");
                response.append(js);
            }
        } catch (IOException e) {
            System.err.println("文件未找到");
            e.printStackTrace();
            return null;
        }
        return String.valueOf(response);
    }

}
