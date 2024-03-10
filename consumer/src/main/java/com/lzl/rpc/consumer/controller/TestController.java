package com.lzl.rpc.consumer.controller;

import com.lzl.rpc.consumer.vo.Resp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public Resp<String> test() {
        return Resp.success("cdscs");
    }
}
