package com.jumia.phonenumbers.service;

import com.jumia.phonenumbers.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Integer getCountOfAllCustomers() {
        return customerRepository.getCountOfAllCustomers();
    }
}
