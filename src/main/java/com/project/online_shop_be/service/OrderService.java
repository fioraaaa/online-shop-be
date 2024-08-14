package com.project.online_shop_be.service;

import com.project.online_shop_be.dto.OrderDto;
import com.project.online_shop_be.model.Customer;
import com.project.online_shop_be.model.Item;
import com.project.online_shop_be.model.Order;
import com.project.online_shop_be.repository.CustomerRepository;
import com.project.online_shop_be.repository.ItemRepository;
import com.project.online_shop_be.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        order.setQuantity(orderDto.getQuantity());

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));
        order.setCustomer(customer);

        Item item = itemRepository.findById(orderDto.getItemsId())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Item tidak ditemukan"));

        // Mengurangi quantity item berdasarkan quantity pada order
        int updatedQuantity = item.getStock() - orderDto.getQuantity();
        if (updatedQuantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock item tidak mencukupi");
        }
        item.setStock(updatedQuantity);

        // Calculate the total price based on quantity and item price
        int totalPrice = orderDto.getQuantity() * item.getPrice();
        order.setTotalPrice(totalPrice);

        // Menyimpan perubahan pada item
        itemRepository.save(item);

        order.setItem(item);

        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order tidak ditemukan"));

        System.out.println("Updating Order with details: " + orderDto);

        // Periksa dan update orderCode jika tersedia
        if (orderDto.getOrderCode() != null) {
            order.setOrderCode(orderDto.getOrderCode());
        }

        // Periksa dan update orderDate jika tersedia
        if (orderDto.getOrderDate() != null) {
            order.setOrderDate(orderDto.getOrderDate());
        }

        // Periksa dan update quantity jika tersedia
        if (orderDto.getQuantity() != null) {
            // Mengembalikan stok item lama sebelum mengupdate item baru
            Item oldItem = order.getItem();
            if (oldItem != null) {
                int restoredStock = oldItem.getStock() + order.getQuantity();
                oldItem.setStock(restoredStock);
                itemRepository.save(oldItem);
            }

            // Periksa dan update item jika itemsId tersedia
            if (orderDto.getItemsId() != null) {
                Item item = itemRepository.findById(orderDto.getItemsId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item tidak ditemukan"));

                int updatedStock = item.getStock() - orderDto.getQuantity();
                if (updatedStock < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock item tidak mencukupi");
                }
                item.setStock(updatedStock);

                // Update item pada order
                order.setItem(item);
                itemRepository.save(item);

                // Recalculate the total price based on new quantity and item price
                int totalPrice = orderDto.getQuantity() * item.getPrice();
                order.setTotalPrice(totalPrice);
            } else {
                // Jika itemsId tidak diberikan, tetap gunakan item yang ada untuk menghitung ulang harga total
                Item currentItem = order.getItem();
                if (currentItem != null) {
                    int totalPrice = orderDto.getQuantity() * currentItem.getPrice();
                    order.setTotalPrice(totalPrice);
                }
            }

            // Update quantity pada order
            order.setQuantity(orderDto.getQuantity());
        }

        // Periksa dan update customer jika customerId tersedia
        if (orderDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));
            order.setCustomer(customer);
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

    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }}
