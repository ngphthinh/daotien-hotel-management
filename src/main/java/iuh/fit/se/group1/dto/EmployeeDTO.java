package iuh.fit.se.group1.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO  implements Serializable {
    private Long employeeId;
    private String fullName;
    private String phone;
    private String email;
    private boolean gender;
    private String citizenId;
    private LocalDate hireDate;
    private AccountDTO account;
    @ToString.Exclude
    private byte[] avt;

}
