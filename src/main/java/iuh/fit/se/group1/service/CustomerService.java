package iuh.fit.se.group1.service;

import java.util.List;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.repository.jpa.CustomerRepositoryImpl;

public class CustomerService extends Service {
    private final CustomerRepositoryImpl customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepositoryImpl();
    }

    public Customer createCustomer(Customer customer) {
        if (getCustomerByCitizenId(customer.getCitizenId()) != null) {
            return null;
        }
        return doInTransaction(entityManager -> customerRepository.save(entityManager, customer));
    }

    public void deleteCustomer(Long customerId) {
//        customerRepository.deleteById(customerId);
        doInTransactionVoid(entityManager -> customerRepository.deleteById(entityManager, customerId));
    }


    public List<Customer> getAllCustomer() {
//        return customerRepository.findAll();
        return doInTransaction(customerRepository::findAll);
    }


    public Customer updateAmenity(Customer customer) {
//        return customerRepository.update(customer);
        return doInTransaction(entityManager -> customerRepository.update(entityManager, customer));
    }

    public List<Customer> getAmenityByKeyword(String keyword) {
//        return customerRepository.findByCustomerNameOrId(keyword);
        return doInTransaction(entityManager -> customerRepository.findByCustomerNameOrId(entityManager, keyword));
    }

    public Customer getCustomerById(String idStr) {
        try {
            Long id = Long.parseLong(idStr);
//            return customerRepository.findById(id);
            return doInTransaction(entityManager -> customerRepository.findById(entityManager, id));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Customer updateCustomer(Customer customer) {
//        return customerRepository.update(customer);
        return doInTransaction(entityManager -> customerRepository.update(entityManager, customer));
    }

    public Customer getCustomerByCitizenId(String citizenId) {
//        return customerRepository.findByCitizenId(citizenId);
        return doInTransaction(entityManager -> customerRepository.findByCitizenId(entityManager, citizenId));
    }

}
