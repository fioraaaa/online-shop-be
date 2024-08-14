package com.project.online_shop_be.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CustomerResponseDto {
    private Long customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private Boolean isActive;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastOrderDate;
    private String pic;

    public CustomerResponseDto(Long customerId, String customerName, String customerAddress,
                               String customerCode, String customerPhone, Boolean isActive,
                               Date lastOrderDate, String pic) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCode = customerCode;
        this.customerPhone = customerPhone;
        this.isActive = isActive;
        this.lastOrderDate = lastOrderDate;
        this.pic = pic;
    }

}

