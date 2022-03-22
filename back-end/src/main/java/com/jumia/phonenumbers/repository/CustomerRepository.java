package com.jumia.phonenumbers.repository;

import com.jumia.phonenumbers.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    @Query("SELECT COUNT(customer) FROM Customer customer")
    Integer getCountOfAllCustomers();

}
