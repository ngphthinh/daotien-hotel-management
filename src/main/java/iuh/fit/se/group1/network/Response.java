package iuh.fit.se.group1.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Response implements Serializable {
    private String requestId;
    private int code;
    private String message;
    private Object data;
}
