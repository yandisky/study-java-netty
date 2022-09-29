package org.study.netty.util;


import org.study.netty.domain.*;

public class MsgUtil {
    /**
     * 构建对象；请求传输文件(客户端)
     */
    public static FileTransferProtocol buildRequestTransferProtocol(String fileUrl, String fileName, Long fileSize) {
        FileDescInfo fileDescInfo = new FileDescInfo();
        fileDescInfo.setFileUrl(fileUrl);
        fileDescInfo.setFileName(fileName);
        fileDescInfo.setFileSize(fileSize);
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.REQUEST);
        fileTransferProtocol.setTransferObj(fileDescInfo);
        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输指令(服务端)
     */
    public static FileTransferProtocol buildTransferInstruct(Integer status, String clientFileUrl, Integer readPosition) {
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(status);
        fileBurstInstruct.setClientFileUrl(clientFileUrl);
        fileBurstInstruct.setReadPosition(readPosition);
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输指令(服务端)
     */
    public static FileTransferProtocol buildTransferInstruct(FileBurstInstruct fileBurstInstruct) {
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.INSTRUCT);
        fileTransferProtocol.setTransferObj(fileBurstInstruct);
        return fileTransferProtocol;
    }

    /**
     * 构建对象；文件传输数据(客户端)
     */
    public static FileTransferProtocol buildTransferData(FileBurstData fileBurstData) {
        FileTransferProtocol fileTransferProtocol = new FileTransferProtocol();
        fileTransferProtocol.setTransferType(Constants.TransferType.DATA);
        fileTransferProtocol.setTransferObj(fileBurstData);
        return fileTransferProtocol;
    }
}
