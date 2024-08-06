package com.project.online_shop_be.controller;

import com.project.online_shop_be.dto.CustomerDto;
import com.project.online_shop_be.model.Customer;
import com.project.online_shop_be.repository.CustomerRepository;
import com.project.online_shop_be.service.CustomerService;
import lib.minio.MinioSrvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final MinioSrvc minioSrvc;


    public CustomerController(CustomerService customerService, CustomerRepository customerRepository, MinioSrvc minioSrvc) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.minioSrvc = minioSrvc;
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@ModelAttribute CustomerDto customerDto,
                                                @RequestParam("file") MultipartFile file) {
        logger.info("Received CustomerDto: {}", customerDto);
        Customer customer = customerService.addCustomer(customerDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId,
                                            @ModelAttribute CustomerDto customerDto,
                                            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Customer customer = customerService.updateCustomer(customerId, customerDto, file);
            return ResponseEntity.ok(customer);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan");
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.ok("Customer berhasil dihapus");
        } catch (Exception ex) {
            logger.error("Error deleting customer with ID " + customerId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Customer tidak dapat dihapus");
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
}
