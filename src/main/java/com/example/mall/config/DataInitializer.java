package com.example.mall.config;

import com.example.mall.order.Order;
import com.example.mall.order.OrderItem;
import com.example.mall.order.OrderRepository;
import com.example.mall.product.Product;
import com.example.mall.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // 初始化商品数据
        Product product1 = new Product();
        product1.setName("iPhone 15 Pro");
        product1.setSku("SKU-IPHONE-001");
        product1.setStock(100);
        product1.setPrice(799900);
        product1 = productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("MacBook Pro 14");
        product2.setSku("SKU-MAC-001");
        product2.setStock(50);
        product2.setPrice(1299900);
        product2 = productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("AirPods Pro");
        product3.setSku("SKU-AIRPODS-001");
        product3.setStock(200);
        product3.setPrice(199900);
        product3 = productRepository.save(product3);

        // 初始化订单数据
        Order order1 = new Order();
        order1.setOrderSn("ORDER-20240318-001");
        order1.setTotalAmount(799900);
        order1.setCreatedAt(LocalDateTime.now());

        OrderItem item1 = new OrderItem();
        item1.setOrder(order1);
        item1.setProduct(product1);
        item1.setQuantity(1);
        item1.setUnitPrice(799900);
        order1.getOrderItems().add(item1);

        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderSn("ORDER-20240318-002");
        order2.setTotalAmount(1499800);
        order2.setCreatedAt(LocalDateTime.now());

        OrderItem item2 = new OrderItem();
        item2.setOrder(order2);
        item2.setProduct(product3);
        item2.setQuantity(2);
        item2.setUnitPrice(199900);
        order2.getOrderItems().add(item2);

        OrderItem item3 = new OrderItem();
        item3.setOrder(order2);
        item3.setProduct(product1);
        item3.setQuantity(1);
        item3.setUnitPrice(799900);
        order2.getOrderItems().add(item3);

        orderRepository.save(order2);

        System.out.println("数据初始化完成！");
    }
}
