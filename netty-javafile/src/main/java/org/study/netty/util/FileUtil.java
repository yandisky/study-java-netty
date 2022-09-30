package org.study.netty.util;

import org.study.netty.domain.Constants;
import org.study.netty.domain.FileBurstData;
import org.study.netty.domain.FileBurstInstruct;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtil {
    public static FileBurstData readFile(String fileUrl, Integer readPosition) throws IOException {
        File file = new File(fileUrl);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(readPosition);
        byte[] bytes = new byte[1024];
        int readSize = randomAccessFile.read(bytes);
        if (readSize <= 0) {
            randomAccessFile.close();
            return new FileBurstData(Constants.FileStatus.COMPLETE);
        }
        FileBurstData fileBurstData = new FileBurstData();
        fileBurstData.setFileUrl(fileUrl);
        fileBurstData.setFileName(file.getName());
        fileBurstData.setBeginPos(readPosition);
        fileBurstData.setEndPos(readPosition + readSize);
        //不足1024需要拷贝去掉空字符
        if (readSize < 1024) {
            byte[] copy = new byte[readSize];
            System.arraycopy(bytes, 0, copy, 0, readSize);
            fileBurstData.setBytes(copy);
            fileBurstData.setStatus(Constants.FileStatus.END);
        } else {
            fileBurstData.setBytes(bytes);
            fileBurstData.setStatus(Constants.FileStatus.CENTER);
        }
        randomAccessFile.close();
        return fileBurstData;
    }

    public static FileBurstInstruct writeFile(String baseUrl, FileBurstData fileBurstData) throws IOException {
        if (Constants.FileStatus.COMPLETE == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }
        File file = new File(baseUrl + "/" + fileBurstData.getFileName());
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(fileBurstData.getBeginPos());//移动文件记录指针的位置
        randomAccessFile.write(fileBurstData.getBytes());//调用了seek（start）方法，是指把文件的记录指针定位到start字节的位置。也就是说程序将从start字节开始写数据
        randomAccessFile.close();
        if (Constants.FileStatus.END == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }
        //文件分片传输指令
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(Constants.FileStatus.CENTER);
        fileBurstInstruct.setClientFileUrl(fileBurstData.getFileUrl());//客户端文件URL
        fileBurstInstruct.setReadPosition(fileBurstData.getEndPos() + 1);//读取位置
        return fileBurstInstruct;
    }
}
