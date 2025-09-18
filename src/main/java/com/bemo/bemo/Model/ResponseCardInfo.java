package com.bemo.bemo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCardInfo {
    private String name;
    private String serial;
    private String code;
    private String expired;
}