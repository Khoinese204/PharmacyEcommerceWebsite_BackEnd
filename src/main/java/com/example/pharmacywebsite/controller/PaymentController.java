package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.config.VNPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private VNPayConfig vnPayConfig;

    @PostMapping("/create-payment")
    public Map<String, Object> createPayment(HttpServletRequest req, @RequestParam("amount") long amount)
            throws Exception {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amountInVND = amount * 100L;

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = req.getRemoteAddr();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInVND));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:3000/checkout/vnpay-return");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        // build data
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = vnPayConfig.vnp_PayUrl + "?" + query;

        Map<String, Object> response = new HashMap<>();
        System.out.println("HashData = " + hashData);
        System.out.println("MyHash = " + vnp_SecureHash);
        System.out.println("PaymentURL = " + paymentUrl);

        response.put("code", "00");
        response.put("message", "success");
        response.put("data", paymentUrl);

        return response;
    }

    @GetMapping("/vnpay-return")
    public Map<String, Object> paymentReturn(@RequestParam Map<String, String> allParams) {
        Map<String, Object> response = new HashMap<>();

        String vnp_SecureHash = allParams.remove("vnp_SecureHash");
        allParams.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(allParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
            String fieldName = itr.next();
            String fieldValue = allParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        String calculatedHash = VNPayConfig.hmacSHA512(vnPayConfig.secretKey, hashData.toString());

        System.out.println("===== VNPAY RETURN =====");
        System.out.println("HashDataReturn = " + hashData);
        System.out.println("MyCalculatedHash = " + calculatedHash);
        System.out.println("VNPayHash = " + vnp_SecureHash);
        System.out.println("========================");

        if (calculatedHash.equals(vnp_SecureHash)) {
            if ("00".equals(allParams.get("vnp_ResponseCode"))) {
                response.put("status", "success");
                response.put("message", "Thanh toán thành công");
            } else {
                response.put("status", "failed");
                response.put("message", "Thanh toán thất bại, mã lỗi: " + allParams.get("vnp_ResponseCode"));
            }
        } else {
            response.put("status", "error");
            response.put("message", "Sai chữ ký bảo mật");
        }

        response.put("data", allParams);
        return response;
    }

}
