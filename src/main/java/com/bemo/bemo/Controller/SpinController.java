package com.bemo.bemo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bemo.bemo.Model.RequestSpin;
import com.bemo.bemo.Model.ResponseSpin;
import com.bemo.bemo.Service.SpinService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin("*")
public class SpinController {

    @Autowired
    private SpinService spinService;

    public boolean gachThe(@RequestBody RequestSpin requestSpin) {
        return spinService.callChargingApi(requestSpin);
    }

    public ResponseSpin doiThe(String amount, String telco) {
        return spinService.callBuyCard(amount, telco);
    }

    public boolean spin(String amount) {
        return spinService.randomSpinResult(Double.parseDouble(amount));
    }

    @PostMapping("/spin")
    public ResponseSpin up(@RequestBody RequestSpin requestSpin) {
        if (!gachThe(requestSpin)) {
            return new ResponseSpin(2, "Nạp thẻ thất bại", null);
        }    

        if (!spin(requestSpin.getAmount())) {
            return new ResponseSpin(3, "Chúc bạn may mắn lần sau", null);
        }

        return doiThe(requestSpin.getAmount(), formatTelco(requestSpin.getTelco()));
    }

    @PostMapping("/testGachThe")
    public boolean testGachThe(@RequestBody RequestSpin requestSpin) {
        return gachThe(requestSpin);
    }

    @GetMapping("/testDoiThe")
    public ResponseSpin testDoiThe() {
        return doiThe("10000", "Viettel");
    }

    private String formatTelco(String telco) {
        if (telco == null || telco.isEmpty())
            return telco;
        return telco.substring(0, 1).toUpperCase() + telco.substring(1).toLowerCase();
    }

}
