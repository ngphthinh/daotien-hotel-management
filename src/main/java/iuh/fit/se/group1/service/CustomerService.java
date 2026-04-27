package iuh.fit.se.group1.service;

import java.util.List;
import java.util.stream.Collectors;

import iuh.fit.se.group1.dto.CustomerDTO;
import iuh.fit.se.group1.mapper.CustomerMapper;
import iuh.fit.se.group1.repository.jpa.CustomerRepositoryImpl;
import iuh.fit.se.group1.repository.jpa.OrderRepositoryImpl;

public class CustomerService extends Service {
    private final CustomerRepositoryImpl customerRepository;
    private final OrderRepositoryImpl orderRepository;

    private final CustomerMapper customerMapper;

    public CustomerService() {
        this.customerMapper = new CustomerMapper();
        this.orderRepository = new OrderRepositoryImpl();
        this.customerRepository = new CustomerRepositoryImpl();
    }

    public CustomerDTO createCustomer(CustomerDTO customer) {
        if (getCustomerByCitizenId(customer.getCitizenId()) != null) {
            return null;
        }
        return doInTransaction(entityManager -> customerMapper.toDTO(customerRepository.save(entityManager, customerMapper.toCustomer(customer))));
    }

    public boolean deleteCustomer(Long customerId) {

        return doInTransaction(entityManager ->
                {

                    boolean exists = orderRepository.existsByCustomerIdAndCompleteYet(entityManager, customerId);
                    if (exists) {
                        return false;
                    }

                    customerRepository.deleteById(entityManager, customerId);
                    return true;
                }
        );
    }


    public List<CustomerDTO> getAllCustomer() {
        return doInTransaction(customerRepository::findAll).stream().map(customerMapper::toDTO).collect(Collectors.toList());
    }


    public List<CustomerDTO> getCustomerByKeyword(String keyword) {
        return doInTransaction(entityManager -> customerRepository.findByCustomerNameOrId(entityManager, keyword).stream().map(customerMapper::toDTO).collect(Collectors.toList()));
    }

    public CustomerDTO getCustomerById(Long id) {

        return doInTransaction(entityManager -> customerMapper.toDTO(customerRepository.findById(entityManager, id)));


    }

    public CustomerDTO updateCustomer(CustomerDTO customer) {
        return doInTransaction(entityManager -> customerMapper.toDTO(customerRepository.save(entityManager, customerMapper.toCustomer(customer))));
    }

    public CustomerDTO getCustomerByCitizenId(String citizenId) {
        return doInTransaction(entityManager -> customerMapper.toDTO(customerRepository.findByCitizenId(entityManager, citizenId)));
    }

}
