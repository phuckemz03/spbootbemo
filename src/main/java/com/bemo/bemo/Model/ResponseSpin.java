package com.bemo.bemo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSpin {
    private int status;
    private String message;
    private DataResponse data;
}
