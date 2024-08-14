package com.project.online_shop_be.service;

import com.project.online_shop_be.model.Order;
import com.project.online_shop_be.repository.OrderRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OrderRepository orderRepository;

    public byte[] generateOrderReport(Long orderId) throws Exception {
        // Load .jrxml template
        Resource resource = resourceLoader.getResource("classpath:reports/order_report.jrxml");
        InputStream inputStream = resource.getInputStream();

        // Compile .jrxml to .jasper
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        // Set parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ORDER_ID", orderId);

        // Fetch data from the database
        List<Order> orders = orderRepository.findByOrderId(orderId);

        // Check if orders is not empty
        if (orders.isEmpty()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        // Provide data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);

        // Fill the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export report to PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

