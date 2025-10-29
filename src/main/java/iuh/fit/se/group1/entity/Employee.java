package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private Long employeeId;
    private String fullName;
    private String phone;
    private String email;
    private boolean gender;
    private String citizenId;
    private LocalDate hireDate;
    private Account account;
    private byte[] avt;
    private LocalDate createdAt;

    public Employee() {
    }

    public Employee(Long employeeId, String fullName, String phone, String email, boolean gender, String citizenId, LocalDate hireDate, Account account, byte[] avt, LocalDate createdAt) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.citizenId = citizenId;
        this.hireDate = hireDate;
        this.account = account;
        this.avt = avt;
        this.createdAt = createdAt;
    }

    public byte[] getAvt() {
        return avt;
    }

    public void setAvt(byte[] avt) {
        this.avt = avt;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isGender() {
        return gender;
    }
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public String getCitizenId() {
        return citizenId;
    }
    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(employeeId, employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(employeeId);
    }

    @Override
    public String toString() {
        return "Employee [employeeId=" + employeeId + ", fullName=" + fullName + ", phone=" + phone + ", email=" + email
                + ", gender=" + gender + ", citizenId=" + citizenId + ", hireDate=" + hireDate + ", account=" + account
                + ", createdAt=" + createdAt + "]";
    }
    
}