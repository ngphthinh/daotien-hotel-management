package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor

@NoArgsConstructor
@Data
@Builder
public class ValidationResult implements Serializable {
    private boolean valid;
    private String message;
}
