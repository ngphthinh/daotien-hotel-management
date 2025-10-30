package iuh.fit.se.group1.config;

import iuh.fit.se.group1.entity.Role;
import iuh.fit.se.group1.service.RoleService;

import java.time.LocalDate;

public class DataInitializer implements Runner {

    static {
        new DataInitializer().run();
    }

    @Override
    public void run() {

        RoleService roleService = new RoleService();
        if (!roleService.roleExists(iuh.fit.se.group1.enums.Role.MANAGER.toString())) {
            Role roleManager = new Role(iuh.fit.se.group1.enums.Role.MANAGER.toString(), "Nhân viên quản lý", LocalDate.now());
            roleService.createRole(roleManager);
        }
        if (!roleService.roleExists(iuh.fit.se.group1.enums.Role.RECEPTIONIST.toString())) {
        Role roleReceptionist = new Role(iuh.fit.se.group1.enums.Role.RECEPTIONIST.toString(), "Nhân viên quản lý", LocalDate.now());
            roleService.createRole(roleReceptionist);
        }


    }
}
