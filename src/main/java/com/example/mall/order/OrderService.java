package com.example.mall.order;

import com.example.mall.common.BusinessException;
import com.example.mall.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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

    public Integer countProductQuantityByConditions(OrderStatisticsQuery query) {
        log.info("开始统计订单商品数量, 查询条件: userId={}, orderSn={}, status={}, startTime={}, endTime={}",
                query.getUserId(), query.getOrderSn(), query.getStatus(), query.getStartTime(), query.getEndTime());

        // 1. 查询符合条件的订单
        List<Order> orders = orderRepository.findByConditions(
                query.getUserId(),
                query.getOrderSn(),
                query.getStatus(),
                query.getStartTime(),
                query.getEndTime()
        );

        // 2. 未查询到订单，抛出异常
        if (orders.isEmpty()) {
            log.warn("未查询到符合条件的订单, userId={}", query.getUserId());
            throw new BusinessException("未查询到符合条件的订单");
        }

        // 3. 统计商品总数量
        int totalQuantity = 0;
        boolean hasItems = false;

        for (Order order : orders) {
            List<OrderItem> items = order.getOrderItems();
            if (items != null && !items.isEmpty()) {
                hasItems = true;
                for (OrderItem item : items) {
                    if (item.getQuantity() != null) {
                        totalQuantity += item.getQuantity();
                    }
                }
            }
        }

        // 4. 订单存在但无商品，抛出异常
        if (!hasItems) {
            log.warn("查询到订单但无商品数据, userId={}, orderCount={}", query.getUserId(), orders.size());
            throw new BusinessException("订单存在但无商品数据");
        }

        log.info("统计完成, 订单数量: {}, 商品总数量: {}", orders.size(), totalQuantity);
        return totalQuantity;
    }
}
