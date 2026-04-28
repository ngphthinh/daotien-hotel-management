package iuh.fit.se.group1.network.client.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.network.CommandType;
import iuh.fit.se.group1.network.Request;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor
public class PaymentServiceClient implements ServiceClient {
    private final ClientSocketManager clientSocketManager;

    public Response createPayment(OrderDTO orderDTO) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PAYMENT_CREATE)
                .request(orderDTO)
                .build()).get();
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Trích giá trị từ JSON (cách đơn giản không dùng parser)
     */
    public String extractJsonValue(String json, String keyPath) {
        try {
            JsonNode node = OBJECT_MAPPER.readTree(json);

            for (String key : keyPath.split("\\.")) {
                node = node.get(key);
                if (node == null) return null;
            }

            return node.asText();
        } catch (Exception e) {
            return null;
        }
    }

    public Image generateQRCodeImage(String text, int width, int height) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public Response queryPayment(String orderId) throws Exception {
        return clientSocketManager.send(Request.builder()
                .commandType(CommandType.PAYMENT_QUERY)
                .request(orderId)
                .build()).get();
    }
}
