package com.power.doc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangzhongguo <zhangzhongguo@kuaishou.com>
 * Created on 2023-07-09
 */
public class FileUtil {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static final int EOF = -1;


    public static String fileRead(String fileName) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName),
                StandardCharsets.UTF_8)) {
            char[] chars = new char[515];
            int count;
            while ((count = inputStreamReader.read(chars)) > 0) {
                builder.append(new String(chars, 0, count));
            }
            return builder.toString();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static void copyInputStreamToFile(InputStream source, File target) throws IOException {
        try (FileOutputStream output = openOutputStream(target)) {
            final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while (EOF != (n = source.read(bytes))) {
                output.write(bytes, 0, n);
            }
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }
        return new FileOutputStream(file);
    }

}
