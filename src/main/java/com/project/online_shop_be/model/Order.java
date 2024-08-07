package com.project.online_shop_be.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(name = "order_code", nullable = false)
    private String orderCode;
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @Column(name = "total_price")
    private Integer totalPrice;
    @Column(name = "quantity")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "items_id", nullable = false)
    private Item item;
}
