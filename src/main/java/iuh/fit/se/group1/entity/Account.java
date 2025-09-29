package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Account {
    private Long accountId;
    private String username;
    private String password;
    private LocalDate createdAt;
    private Role role;
    public Account() {
    }
    public Account(Long accountId, String username, String password, LocalDate createdAt, Role role) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountId);
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", username=" + username + ", password=" + password + ", createdAt="
                + createdAt + ", role=" + role + "]";
    }
    
}
