package com.lzl.core.serialization;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.ServerException;

public class HessianSerialization implements RpcSerialization{


    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            throw new NullPointerException();
        }
        byte[] results;

        HessianSerializerOutput hessianOutput;
        SerializerFactory serializerFactory = new SerializerFactory();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            results = os.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return results;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        if (data == null) {
            throw new NullPointerException();
        }
        T result;
        try (ByteArrayInputStream is  = new ByteArrayInputStream(data)) {
            HessianSerializerInput hessianInput = new HessianSerializerInput(is);
            System.out.println("-----" + hessianInput);
            result = (T) hessianInput.readObject(clz);
            System.out.println("++++" + result);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return result;
    }
}
