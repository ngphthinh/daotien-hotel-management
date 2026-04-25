package iuh.fit.se.group1.mapper;

import iuh.fit.se.group1.dto.CustomerDTO;
import iuh.fit.se.group1.entity.Customer;

public class CustomerMapper {
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null)
            return null;
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.isGender())
                .citizenId(customer.getCitizenId())
                .fullName(customer.getFullName())
                .build();
    }

    public Customer toCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null)
            return null;
        return Customer.builder()
                .customerId(customerDTO.getCustomerId())
                .phone(customerDTO.getPhone())
                .email(customerDTO.getEmail())
                .dateOfBirth(customerDTO.getDateOfBirth())
                .gender(customerDTO.isGender())
                .citizenId(customerDTO.getCitizenId())
                .fullName(customerDTO.getFullName())
                .build();
    }
}
