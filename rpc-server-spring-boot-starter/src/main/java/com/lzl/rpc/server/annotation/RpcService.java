package com.lzl.rpc.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RpcService {

    Class<?> interfaceType() default Object.class;

    String version() default "1.0";
}
