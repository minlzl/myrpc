package com.lzl.core.serialization.time;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class LocalDateTimeDeserializer extends AbstractDeserializer {
    @Override
    public Class getType()
    {
        return LocalDateTime.class;
    }


    @Override
    public Object readObject(AbstractHessianInput in,
                             Object []fields)
            throws IOException
    {
        String []fieldNames = (String []) fields;

        int ref = in.addRef(null);

        long initValue = Long.MIN_VALUE;

        for (int i = 0; i < fieldNames.length; i++) {
            String key = fieldNames[i];

            if (key.equals("value")) {
                initValue = in.readUTCDate();
            } else {
                in.readObject();
            }
        }
        Object value = create(initValue);
        in.setRef(ref, value);
        return value;
    }

    @Override
    public Object readMap(AbstractHessianInput in) throws IOException {
        Map map = new HashMap();

        in.addRef(map);

        while (! in.isEnd()) {
            map.put(in.readObject(), in.readObject());
        }
        in.readEnd();
        return map;
    }


    private Object create(long initValue)
            throws IOException
    {
        if (initValue == Long.MIN_VALUE) {
            throw new IOException(LocalDateTime.class + " expects name.");
        }
        try {
            return LocalDateTime.ofEpochSecond(new Long(initValue)/1000,Integer.valueOf(String.valueOf(initValue%1000))*1000, ZoneOffset.of("+8"));
        } catch (Exception e) {
            throw new IOExceptionWrapper(e);
        }
    }
}
