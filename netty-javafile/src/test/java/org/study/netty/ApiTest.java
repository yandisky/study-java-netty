package org.study.netty;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

public class ApiTest {
    public static void main(String[] args) throws IOException {
        test("E:\\image\\zu.png");
    }

    private static void test(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(0);
        byte[] bytes = new byte[1024];
        int byteRead = randomAccessFile.read(bytes);
        System.out.println(fileUrl);
        System.out.println("读取文件长度：" + byteRead);
        for (byte b : bytes) {
            System.out.println(new BigInteger(1, new byte[]{b}).toString(16) + " ");
        }
        System.out.println("\r\n");
    }
}
