package org.study.netty;

import com.googlecode.protobuf.format.JsonFormat;
import org.study.netty.domain.MsgBody;

public class ApiTest {
    public static void main(String[] args) throws JsonFormat.ParseException {
        MsgBody.Builder msg = MsgBody.newBuilder();
        msg.setChannelId("001");
        msg.setMsgInfo("HelloWorld");
        MsgBody msgBody = msg.build();
        //protobuf转Json 需要引入protobuf-java-format
        String msgBodyStr = JsonFormat.printToString(msgBody);
        System.out.println(msgBodyStr);
        //json转protobuf 需要引入protobuf-java-format
        JsonFormat.merge("{\"channelId\": \"002\",\"msgInfo\": \"Hi Netty\"}", msg);
        msgBody = msg.build();
        System.out.println(msgBody.getChannelId());
        System.out.println(msgBody.getMsgInfo());
    }
}
