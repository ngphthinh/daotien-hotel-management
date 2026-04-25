package iuh.fit.se.group1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    private Long employeeId;
    private String fullName;
    private String phone;
    private String email;
    private boolean gender;
    private String citizenId;
    private LocalDate hireDate;
    private AccountDTO account;
    private byte[] avt;

}
