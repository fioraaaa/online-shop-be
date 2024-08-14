package com.project.online_shop_be.controller;

import com.project.online_shop_be.dto.CustomerDto;
import com.project.online_shop_be.dto.CustomerResponseDto;
import com.project.online_shop_be.model.Customer;
import com.project.online_shop_be.model.Item;
import com.project.online_shop_be.repository.CustomerRepository;
import com.project.online_shop_be.service.CustomerService;
import lib.minio.MinioSrvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                                                @RequestParam("pic") MultipartFile pic) {
        logger.info("Received CustomerDto: {}", customerDto);
        Customer customer = customerService.addCustomer(customerDto, pic);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId,
                                            @ModelAttribute CustomerDto customerDto,
                                            @RequestParam(value = "pic", required = false) MultipartFile pic) {
        try {
            Customer customer = customerService.updateCustomer(customerId, customerDto, pic);
            return ResponseEntity.ok(customer);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan");
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception ex) {
            logger.error("Error deleting customer with ID " + customerId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long customerId) {
        CustomerResponseDto customerDto = customerService.getCustomerById(customerId);
        return customerDto != null ? ResponseEntity.ok(customerDto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<CustomerResponseDto> customerPage = customerService.getCustomers(page, size);
        return ResponseEntity.ok(customerPage);
    }
}
