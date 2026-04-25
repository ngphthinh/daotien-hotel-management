package iuh.fit.se.group1.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerDTO {
    private Long customerId;
    private String fullName;
    private String phone;
    private String email;
    private String citizenId;
    private boolean gender;
    private LocalDate dateOfBirth;
}
