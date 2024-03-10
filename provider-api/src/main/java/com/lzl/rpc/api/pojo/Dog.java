package com.lzl.rpc.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dog implements Serializable {

    String name;
    Integer age;
    List<String> pans;
    Home home;
}
