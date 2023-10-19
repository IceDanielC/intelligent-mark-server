package com.cyb.mongodb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 双token实现无感刷新
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoubleToken {
    private String accessToken;

    private String freshToken;
}
