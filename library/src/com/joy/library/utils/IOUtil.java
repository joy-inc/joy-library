package com.joy.library.utils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO工具类
 */
public class IOUtil {

    public static void closeInStream(InputStream input) {

        try {

            if (input != null)
                input.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void closeOutStream(OutputStream output) {

        try {

            if (output != null)
                output.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}