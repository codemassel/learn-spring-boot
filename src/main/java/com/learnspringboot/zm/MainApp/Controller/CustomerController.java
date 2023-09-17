package com.learnspringboot.zm.MainApp.Controller;

import com.learnspringboot.zm.MainApp.Entity.Customer;
import com.learnspringboot.zm.MainApp.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;


    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fillCustomers")
    public void fillCustomers() {
        //4 Customers erstellen und in DB einfügen
        Customer customer1 = new Customer();
        customer1.setVorname("Marcel");
        customer1.setNachname("Zimmermann");
        customerService.saveCustomer(customer1);
        System.out.println("Customer "+ customer1.getVorname() + " " + customer1.getNachname() +" angelegt!");

        Customer customer2 = new Customer();
        customer2.setVorname("Ehsan");
        customer2.setNachname("Moradi");
        customerService.saveCustomer(customer2);
        System.out.println("Customer "+ customer2.getVorname() + " " + customer2.getNachname() +" angelegt!");

        Customer customer3 = new Customer();
        customer3.setVorname("Thomas");
        customer3.setNachname("Schäfer");
        customerService.saveCustomer(customer3);
        System.out.println("Customer "+ customer3.getVorname() + " " + customer3.getNachname() +" angelegt!");

        Customer customer4 = new Customer();
        customer4.setVorname("Sven");
        customer4.setNachname("Domberg");
        customerService.saveCustomer(customer4);
        System.out.println("Customer "+ customer4.getVorname() + " " + customer4.getNachname() +" angelegt!");

        System.out.println("-----------------------------------------------------------");
        long customerCount = customerService.countCustomers();
        System.out.println("Aktuell gibt es also " + customerCount + " Customers.");

        System.out.println("-----------------------------------------------------------");
        customerService.deleteCustomerById(1L);
        System.out.println("Oha, ein Customer wurde deleted! Kataschtroph");

        System.out.println("-----------------------------------------------------------");
        customerCount = customerService.countCustomers();
        System.out.println("Jetzt gibt es also nur noch "+ customerCount + " Customers!");
    }

    @GetMapping("/showCustomers")
    public String getAllCustomers(Model model) {
        // Liste aller Kunden aus der Datenbank abrufen
        List<Customer> customers = customerService.getAllCustomers();

        // Die Liste an das Model binden, um sie in der HTML-Seite anzuzeigen, die Variable customers kann jetzt in der HTML file genutzt werden
        model.addAttribute("customers", customers);

        // Die HTML-Seite zum Anzeigen der Kundenliste anzeigen (customers.html)
        return "customers";
    }
}
