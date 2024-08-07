package com.project.online_shop_be.service;

import com.project.online_shop_be.dto.OrderDto;
import com.project.online_shop_be.model.Customer;
import com.project.online_shop_be.model.Item;
import com.project.online_shop_be.model.Order;
import com.project.online_shop_be.repository.CustomerRepository;
import com.project.online_shop_be.repository.ItemRepository;
import com.project.online_shop_be.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order addOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderCode(orderDto.getOrderCode());
        order.setOrderDate(orderDto.getOrderDate());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setQuantity(orderDto.getQuantity());

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));
        order.setCustomer(customer);

        Item item = itemRepository.findById(orderDto.getItemsId())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Item tidak ditemukan"));
        order.setItem(item);

        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order tidak ditemukan"));

        System.out.println("Updating Order with details: " + orderDto);

        if (orderDto.getOrderCode() != null) {
            order.setOrderCode(orderDto.getOrderCode());
        }
        if (orderDto.getOrderDate() != null) {
            order.setOrderDate(orderDto.getOrderDate());
        }
        if (orderDto.getTotalPrice() != null) {
            order.setTotalPrice(orderDto.getTotalPrice());
        }
        if (orderDto.getQuantity() != null) {
            order.setQuantity(orderDto.getQuantity());
        }
        if (orderDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));
            order.setCustomer(customer);
        }
        if (orderDto.getItemsId() != null) {
            Item item = itemRepository.findById(orderDto.getItemsId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item tidak ditemukan"));
            order.setItem(item);
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
