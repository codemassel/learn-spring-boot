package com.learnspringboot.zm.MainApp.Service;

import com.learnspringboot.zm.MainApp.Entity.Customer;
import com.learnspringboot.zm.MainApp.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    //Diese Klasse ist nur zu Beispielzwecken gedacht, eigentlich ist sie überflüssig, da uns JpaRepository genau diese Methoden schon zur Verfügung stellt

    private final CustomerRepository customerRepository;

    /*Constructor Dependency-Injection, @Autowired durchsucht das Projekt selbst nach CustomerRepository.
    Dependency-Injection über den Constructor ist die sauberste Variante und sollte wo möglich immer genutzt werden.
    */
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public long countCustomers() {
        return customerRepository.count();
    }

    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> getAllCustomers() {
        // Diese Methode gibt alle Kunden aus der Datenbank zurück
        return customerRepository.findAll();
    }
}
