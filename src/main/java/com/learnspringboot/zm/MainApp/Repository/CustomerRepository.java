package com.learnspringboot.zm.MainApp.Repository;

import com.learnspringboot.zm.MainApp.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
