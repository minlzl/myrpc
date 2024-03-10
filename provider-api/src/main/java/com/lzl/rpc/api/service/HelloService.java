package com.lzl.rpc.api.service;

import com.lzl.rpc.api.pojo.Dog;

public interface HelloService {
    String sayHello();

    Dog sing(Dog dog);
}
