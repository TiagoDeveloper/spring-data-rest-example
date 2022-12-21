package com.tiagodeveloper.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tiagodeveloper.entity.Customer;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID> {

}
