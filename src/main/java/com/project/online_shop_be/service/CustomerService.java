package com.project.online_shop_be.service;

import com.project.online_shop_be.dto.CustomerDto;
import com.project.online_shop_be.dto.CustomerResponseDto;
import com.project.online_shop_be.model.Customer;
import com.project.online_shop_be.repository.CustomerRepository;
import lib.minio.MinioSrvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    private MinioSrvc minioSrvc;
    @Value("${minio.bucket.name}")
    private String bucketName;

    @Autowired
    public CustomerService(MinioSrvc minioSrvc, CustomerRepository customerRepository) {
        this.minioSrvc = minioSrvc;
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(CustomerDto customerDto, MultipartFile pic) {
        String bucketName = "online-shop";
        String fileName = System.currentTimeMillis() + "_-_" + pic.getOriginalFilename().replace(" ", "_");
        minioSrvc.upload(pic, bucketName, o -> MinioSrvc.UploadOption.builder().filename(fileName).build());

        Customer customer = new Customer();
        customer.setCustomerName(customerDto.getCustomerName());
        customer.setCustomerAddress(customerDto.getCustomerAddress());
        customer.setCustomerCode(customerDto.getCustomerCode());
        customer.setCustomerPhone(customerDto.getCustomerPhone());
        customer.setIsActive(customerDto.getIsActive());
        customer.setLastOrderDate(customerDto.getLastOrderDate());
        customer.setPic(fileName);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, CustomerDto customerDto, MultipartFile pic) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));

        System.out.println("Updating customer with details: " + customerDto);

        if (customerDto.getCustomerName() != null) {
            customer.setCustomerName(customerDto.getCustomerName());
        }
        if (customerDto.getCustomerAddress() != null) {
            customer.setCustomerAddress(customerDto.getCustomerAddress());
        }
        if (customerDto.getCustomerCode() != null) {
            customer.setCustomerCode(customerDto.getCustomerCode());
        }
        if (customerDto.getCustomerPhone() != null) {
            customer.setCustomerPhone(customerDto.getCustomerPhone());
        }
        if (customerDto.getIsActive() != null) {
            customer.setIsActive(customerDto.getIsActive());
        }
        if (customerDto.getLastOrderDate() != null) {
            customer.setLastOrderDate(customerDto.getLastOrderDate());
        }

        // Handle file upload if present
        if (customerDto.getPic() != null && !customerDto.getPic().isEmpty()) {
            String bucketName = "online-shop";
            String fileName = System.currentTimeMillis() + "_-_" + pic.getOriginalFilename().replace(" ", "_");
            minioSrvc.upload(pic, bucketName, o -> MinioSrvc.UploadOption.builder().filename(fileName).build());
            customer.setPic(fileName);
        }

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    public boolean existsById(Long customerId) {
        return customerRepository.existsById(customerId);
    }
    public CustomerResponseDto getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    String picUrl = minioSrvc.getLink("online-shop", customer.getPic(), 3600L);
                    return new CustomerResponseDto(
                            customer.getCustomerId(),
                            customer.getCustomerName(),
                            customer.getCustomerAddress(),
                            customer.getCustomerCode(),
                            customer.getCustomerPhone(),
                            customer.getIsActive(),
                            customer.getLastOrderDate(),
                            picUrl
                    );
                })
                .orElse(null);
    }

    public Page<CustomerResponseDto> getCustomers(int page, int size) {
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));
        return customerPage.map(customer -> {
            String bucketName = "online-shop";
            String pic = minioSrvc.getLink(bucketName, customer.getPic(), 3600L);
            return new CustomerResponseDto(
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getCustomerAddress(),
                    customer.getCustomerCode(),
                    customer.getCustomerPhone(),
                    customer.getIsActive(),
                    customer.getLastOrderDate(),
                    pic
            );
        });
    }
}
