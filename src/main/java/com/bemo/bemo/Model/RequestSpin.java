package com.bemo.bemo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSpin {
    String serial;
    String request_id;
    String telco;
    String code;
    String amount;
    String partner_id;
    String sign;
    String command;
}
