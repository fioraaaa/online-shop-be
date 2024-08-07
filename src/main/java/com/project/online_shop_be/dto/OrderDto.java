package com.project.online_shop_be.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OrderDto {
    private String orderCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
    private Integer totalPrice;
    private Integer quantity;
    private Long customerId;
    private Long itemsId;
}
