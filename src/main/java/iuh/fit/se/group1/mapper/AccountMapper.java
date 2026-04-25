package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.AccountDTO;
import iuh.fit.se.group1.dto.RoleDTO;
import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Role;

public class AccountMapper {
    public AccountDTO toDTO(Account account) {
        if (account == null) return null;

        return AccountDTO.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .role(RoleDTO.builder()
                        .roleName(account.getRole().getRoleName())
                        .roleId(account.getRole().getRoleId())
                        .build())
                .build();
    }

    public Account toAccount(AccountDTO accountDTO) {
        if (accountDTO == null) return null;

        return Account.builder()
                .accountId(accountDTO.getAccountId())
                .username(accountDTO.getUsername())
                .role(Role.builder()
                        .roleName(accountDTO.getRole().getRoleName())
                        .roleId(accountDTO.getRole().getRoleId())
                        .build())
                .build();

    }
}
