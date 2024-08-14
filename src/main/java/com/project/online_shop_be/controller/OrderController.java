package com.project.online_shop_be.controller;

import com.project.online_shop_be.dto.OrderDto;
import com.project.online_shop_be.model.Item;
import com.project.online_shop_be.model.Order;
import com.project.online_shop_be.repository.OrderRepository;
import com.project.online_shop_be.service.OrderService;
import com.project.online_shop_be.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ReportService reportService;

    public OrderController(OrderService orderService, OrderRepository orderRepository, ReportService reportService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto orderDto) {
        logger.info("Received OrderDto: {}", orderDto);
        Order order = orderService.addOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderDto orderDto) {
        try {
            Order order = orderService.updateOrder(orderId, orderDto);
            return ResponseEntity.ok(order);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan");
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception ex) {
            logger.error("Error deleting order with ID " + orderId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Order> ordersPage = orderService.getAllOrders(page, size);
        return ResponseEntity.ok(ordersPage);
    }

    @GetMapping("/report/{orderId}")
    public ResponseEntity<byte[]> downloadOrderReport(@PathVariable Long orderId) {
        try {
            byte[] pdfBytes = reportService.generateOrderReport(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "order_report_" + orderId + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
