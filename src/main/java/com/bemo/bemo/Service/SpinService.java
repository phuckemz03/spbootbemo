package com.bemo.bemo.Service;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import com.bemo.bemo.Model.RequestSpin;
import com.bemo.bemo.Model.ResponseSpin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.MessageDigest;

@Service
public class SpinService {

    private static final String API_URL_CHARGING = "https://apithe.com/chargingws/v2";
    private static final String PARTNER_ID = "100232";
    private static final String COMMAND = "charging";
    private static final String PARTNER_KEY = "T7D1rx2CECGviT2";
    private static final String API_URL_BUY = "https://card24h.com/api/cardws"
            + "?partner_id=39888796525"
            + "&command=buycard"
            + "&request_id=113"
            + "&wallet_number=0011067906"
            + "&qty=2"
            + "&sign=97d3b6e740203606189eaf04fb653805";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public SpinService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    private int getWinRateByPrice(double price) {
        if (price == 10000)
            return 30;
        if (price == 20000)
            return 25;
        if (price == 50000)
            return 20;
        if (price == 100000)
            return 15;
        if (price == 200000)
            return 10;
        if (price == 500000)
            return 5;
        return 0;
    }

    public boolean randomSpinResult(double price) {
        int winRate = getWinRateByPrice(price);
        int random = new Random().nextInt(100) + 1;

        return random <= winRate;
    }

    public boolean callChargingApi(RequestSpin requestSpin) {
        try {

            requestSpin.setRequest_id(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            requestSpin.setPartner_id(PARTNER_ID);
            requestSpin.setCommand(COMMAND);

            String rawData = PARTNER_KEY + requestSpin.getCode().trim() + requestSpin.getSerial().trim();
            String sign = hashMD5(rawData);
            requestSpin.setSign(sign);

            String jsonPayload = mapper.writeValueAsString(requestSpin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL_CHARGING,
                    HttpMethod.POST,
                    entity,
                    String.class);
                    
            System.out.println("Response: " + response.getBody());
            JsonNode jsonNode = mapper.readTree(response.getBody());
            String status = jsonNode.path("status").asText();

            return "99".equals(status);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 error", e);
        }
    }

    public ResponseSpin callBuyCard(String value, String telco) {
        try {
            String url = API_URL_BUY
                    + "&service_code=" + telco
                    + "&value=" + value;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    String.class);
            System.out.println("Response: " + response.getBody());
            JsonNode jsonNode = mapper.readTree(response.getBody());
            String status = jsonNode.path("status").asText();
            if (status.equals("1")) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getBody(), ResponseSpin.class);
            } else {
                return new ResponseSpin(3, "Rút thất bại", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseSpin(3, "Lỗi parse dữ liệu", null);
        }
    }
}
