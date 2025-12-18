package iuh.fit.se.group1.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.util.PropertiesReader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PaymentService {

    private static final String ENDPOINT_CREATE = PropertiesReader.getInstance().get("momo.endpoint.create");
    private static final String ENDPOINT_QUERY = PropertiesReader.getInstance().get("momo.endpoint.query");
    private static final String PARTNER_CODE = PropertiesReader.getInstance().get("momo.partner.code");
    private static final String ACCESS_KEY = PropertiesReader.getInstance().get("momo.access.key");
    private static final String SECRET_KEY = PropertiesReader.getInstance().get("momo.secret.key");

    private static final Gson gson = new Gson();

    public PaymentService() {

    }

    public String createPayment(Order order) throws Exception {

        String orderId = "HD:" + order.getOrderId() + "_" + System.currentTimeMillis();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderInfo = "Thanh toán đơn hàng " + orderId;
        String redirectUrl = "https://momo.vn/return";
        String ipnUrl = "https://webhook.site/unique-id";

        BigDecimal amount = order.getTotalAmount()
//                .subtract(order.getDeposit())
                .setScale(0, RoundingMode.HALF_UP);

        String rawHash = String.format(
                "accessKey=%s&amount=%s&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=captureWallet",
                ACCESS_KEY,
                amount.toPlainString(),
                ipnUrl,
                orderId,
                orderInfo,
                PARTNER_CODE,
                redirectUrl,
                requestId
        );

        String signature = hmacSHA256(SECRET_KEY, rawHash);

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("partnerCode", PARTNER_CODE);
        jsonRequest.addProperty("partnerName", "Dao Tien Hotel");
        jsonRequest.addProperty("storeId", "defaultStore");
        jsonRequest.addProperty("requestId", requestId);
        jsonRequest.addProperty("amount", amount);   // <── Dùng đúng amount
        jsonRequest.addProperty("orderId", orderId);
        jsonRequest.addProperty("orderInfo", orderInfo);
        jsonRequest.addProperty("redirectUrl", redirectUrl);
        jsonRequest.addProperty("ipnUrl", ipnUrl);
        jsonRequest.addProperty("lang", "vi");
        jsonRequest.addProperty("extraData", "");
        jsonRequest.addProperty("requestType", "captureWallet");
        jsonRequest.addProperty("signature", signature);

        return sendPost(ENDPOINT_CREATE, gson.toJson(jsonRequest));
    }

    /** Tạo chữ ký HMAC-SHA256 */
    private String hmacSHA256(String key, String data) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);

        byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /** Gửi POST request */
    private String sendPost(String endpoint,String jsonRequest) throws IOException {
        System.out.println("=== [MoMo REQUEST] ===");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Payload: " + jsonRequest);

        URL url = URI.create(endpoint).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = conn.getResponseCode();
        InputStream responseStream = (statusCode >= 200 && statusCode < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);

            System.out.printf("=== [MoMo RESPONSE] %d ===%n%s%n", statusCode, response);
            return response.toString();
        }
    }

    /** Trích giá trị từ JSON (cách đơn giản không dùng parser) */
    public String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey);
            if (start == -1) return null;
            start += searchKey.length();

            while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
            if (start >= json.length()) return null;

            if (json.charAt(start) == '"') {
                int end = json.indexOf('"', start + 1);
                return (end == -1) ? null : json.substring(start + 1, end);
            }

            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) end++;
            return json.substring(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image generateQRCodeImage(String text, int width, int height) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        return image;
    }

    public String queryPayment(String orderId) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());

        String rawHash = "accessKey=" + ACCESS_KEY +
                "&orderId=" + orderId +
                "&partnerCode=" + PARTNER_CODE +
                "&requestId=" + requestId;

        String signature = hmacSHA256(SECRET_KEY, rawHash);

        String jsonRequest = "{"
                + "\"partnerCode\":\"" + PARTNER_CODE + "\","
                + "\"requestId\":\"" + requestId + "\","
                + "\"orderId\":\"" + orderId + "\","
                + "\"signature\":\"" + signature + "\""
                + "}";

        return sendPost(ENDPOINT_QUERY,jsonRequest);
    }
}