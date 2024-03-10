package com.lzl.rpc.api.service;

import com.lzl.rpc.api.pojo.Dog;
import com.lzl.rpc.api.pojo.History;

import java.time.LocalDateTime;

public interface TestService {

    Dog getDog();

    LocalDateTime getTime(LocalDateTime localDateTime);

    History getHistory();
}
