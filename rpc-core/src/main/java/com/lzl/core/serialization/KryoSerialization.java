package com.lzl.core.serialization;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.K;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoSerialization implements RpcSerialization {

    private final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<>(){
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            kryo.setRegistrationRequired(false);
            kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };
    private final ThreadLocal<Output> outputThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Input> inputThreadLocal = new ThreadLocal<>();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        Kryo kryo = kryoThreadLocal.get();
        kryo.register(obj.getClass());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeObject(output, obj);
        output.close();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Kryo kryo = kryoThreadLocal.get();
        kryo.register(clz);
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Input input = new Input(bis);
        T t = kryo.readObject(input, clz);
        input.close();
        return t;
    }

}
