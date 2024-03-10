package com.lzl.rpc.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class History implements Serializable {
    Long id;

    Long userId;

    String ipAddr;

    String type;

    Long contentId;

    LocalDateTime createTime;
}
