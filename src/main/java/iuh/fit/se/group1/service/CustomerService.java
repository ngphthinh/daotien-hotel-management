package iuh.fit.se.group1.service;

import java.util.List;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.repository.CustomerRepository;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepository();
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }


    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }


    public Customer updateAmenity(Customer customer) {
        return customerRepository.update(customer);
    }

    public List<Customer> getAmenityByKeyword(String keyword) {
        return customerRepository.findByCustomerNameOrId(keyword);
    }

    public Customer getCustomerByCitizenId(String citizenId) {
        return customerRepository.findByCitizenId(citizenId);
    }

}
