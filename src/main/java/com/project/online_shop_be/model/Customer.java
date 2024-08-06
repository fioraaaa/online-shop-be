package com.project.online_shop_be.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;
    @Column(name = "customer_address")
    private String customerAddress;
    @Column(name = "customer_code", nullable = false)
    private String customerCode;
    @Column(name = "customer_phone")
    private String customerPhone;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "last_order_date")
    private Date lastOrderDate;
    @Column(name = "pic")
    private String pic;
}
