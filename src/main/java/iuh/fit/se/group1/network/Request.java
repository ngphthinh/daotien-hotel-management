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
public class Request implements Serializable {
    private String requestId;
    private CommandType commandType;
    private Object request;
}
