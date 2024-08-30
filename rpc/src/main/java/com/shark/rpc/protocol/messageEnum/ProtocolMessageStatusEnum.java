package com.shark.rpc.protocol.messageEnum;

import lombok.Getter;

/**
 * 协议状态码枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;
    private final int value;
    ProtocolMessageStatusEnum(String text,int value){
        this.text = text;
        this.value =value;
    }

    /**
     * 根据value获取枚举类
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        for (ProtocolMessageStatusEnum anEnum: ProtocolMessageStatusEnum.values()){
            if (anEnum.value == value){
                return anEnum;
            }
        }
        return null;
    }

}
