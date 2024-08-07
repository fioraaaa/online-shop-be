package com.project.online_shop_be.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemsId;
    @Column(name = "items_name", nullable = false)
    private String itemsName;
    @Column(name = "items_code", nullable = false)
    private String itemsCode;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "price")
    private Integer price;
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
    @Column(name = "last_re_stock")
    private Date lastReStock;
    @OneToMany(mappedBy = "item")
    private Set<Order> orders;
}
