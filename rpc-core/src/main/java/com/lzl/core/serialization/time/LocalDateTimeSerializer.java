package com.lzl.core.serialization.time;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer extends AbstractSerializer {
    @Override
    public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException
    {
        if (obj == null) {
            out.writeNull();
        } else {
            Class cl = obj.getClass();

            if (out.addRef(obj)) {
                return;
            }
            // ref 返回-2 便是开始写Map
            int ref = out.writeObjectBegin(cl.getName());

            if (ref < -1) {
                out.writeString("value");
                Long milliSecond = ((LocalDateTime) obj).toInstant(ZoneOffset.of("+8")).toEpochMilli();
                out.writeUTCDate(milliSecond);
                out.writeMapEnd();
            } else {
                if (ref == -1) {
                    out.writeInt(1);
                    out.writeString("value");
                    out.writeObjectBegin(cl.getName());
                }

                Long milliSecond = ((LocalDateTime) obj).toInstant(ZoneOffset.of("+8")).toEpochMilli();
                out.writeUTCDate(milliSecond);
            }
        }
    }
}
