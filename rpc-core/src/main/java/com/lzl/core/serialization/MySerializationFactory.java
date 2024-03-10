package com.lzl.core.serialization;

public class MySerializationFactory {

    public static RpcSerialization getRpcSerialization(SerializationTypeEnum typeEnum) {
        switch (typeEnum) {
            case HESSIAN:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            case KRYO:
                return new KryoSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal");
        }
    }
}
