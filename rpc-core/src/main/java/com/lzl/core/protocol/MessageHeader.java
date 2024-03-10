package com.lzl.core.protocol;

import com.lzl.core.serialization.SerializationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageHeader implements Serializable {

    private short magic;
    private byte version;
    private byte serialization;
    private byte msgType;
    private byte status;
    private String requestId;
    private int magLen;

    public static MessageHeader build(String serialization) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMagic(ProtocolConstants.MAGIC);
        messageHeader.setVersion(ProtocolConstants.VERSION);
        messageHeader.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
        messageHeader.setMsgType(MsgType.REQUEST.getType());
        messageHeader.setSerialization(SerializationTypeEnum.parseByName(serialization).getType());
        return messageHeader;
    }

}
