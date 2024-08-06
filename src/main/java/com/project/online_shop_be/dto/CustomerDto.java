package com.project.online_shop_be.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CustomerDto {
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private Boolean isActive;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastOrderDate;
    private MultipartFile file;

}
