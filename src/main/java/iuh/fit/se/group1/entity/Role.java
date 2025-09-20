package iuh.fit.se.group1.entity;

import java.time.LocalDate;

public class Role {
    private String roleId;
    private String roleName;
    private LocalDate createdAt;

    public Role() {
    }

    public Role(String roleId, String roleName, LocalDate createdAt) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdAt = createdAt;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Role [roleId=" + roleId + ", roleName=" + roleName + ", createdAt=" + createdAt + "]";
    }
    
    
}
