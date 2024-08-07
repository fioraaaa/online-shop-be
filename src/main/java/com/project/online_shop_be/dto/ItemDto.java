package com.project.online_shop_be.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ItemDto {
    private String itemsName;
    private String itemsCode;
    private Integer stock;
    private Integer price;
    private Boolean isAvailable;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastReStock;
}
