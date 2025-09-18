package com.bemo.bemo.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {
    private List<ResponseCardInfo> cards;
    private String time;
    private String request_id;
    private String order_code;
}
