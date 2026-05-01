package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticateRequest implements Serializable {
    private String username;
    private String password;
}
