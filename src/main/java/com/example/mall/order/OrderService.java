package com.example.mall.order;

import com.example.mall.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found, id=" + id));
    }

    @Transactional
    public Order create(Order order) {
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, Order updated) {
        Order existing = findById(id);
        existing.setOrderSn(updated.getOrderSn());
        existing.setTotalAmount(updated.getTotalAmount());
        if (updated.getOrderItems() != null) {
            existing.getOrderItems().clear();
            existing.getOrderItems().addAll(updated.getOrderItems());
        }
        return orderRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
