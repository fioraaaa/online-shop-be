package com.project.online_shop_be.repository;

import com.project.online_shop_be.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
