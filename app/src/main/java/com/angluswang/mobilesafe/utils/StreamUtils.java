package com.angluswang.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jeson on 2016/6/2.
 * Stream流工具
 */

public class StreamUtils {

    /**
     * 将输入流读取成String并返回
     * @param in
     * @return
     * @throws IOException
     */
    public static String ReadFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        String result = out.toString();
        in.close();
        out.close();

        return result;
    }
}
