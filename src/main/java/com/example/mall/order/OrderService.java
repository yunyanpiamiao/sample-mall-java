package com.example.mall.order;

import com.example.mall.common.BusinessException;
import com.example.mall.common.EUtill;
import com.example.mall.common.ResourceNotFoundException;
import com.example.mall.common.ResultCode;
import com.example.mall.common.SpecBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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
        log.info("开始统计订单商品数量, 查询条件: {}", query);

        // 构建动态查询条件
        Specification<Order> spec = SpecBuilder.<Order>ins(Order.class)
                .addEq("userId", query.getUserId())
                .addLike("orderSn", query.getOrderSn())
                .addEq("status", query.getStatus())
                .addGreaterThanOrEqual("createdAt", query.getStartTime())
                .addLessThanOrEqual("createdAt", query.getEndTime())
                .builder();

        // 查询符合条件的订单
        List<Order> orders = orderRepository.findAll(spec);

        // 校验：未查询到订单
        EUtill.throwIf(orders.isEmpty(), ResultCode.ORDER_NOT_FOUND.getCode(),
                "未查询到符合条件的订单");

        // 统计商品总数量
        int totalQuantity = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();

        // 校验：订单存在但无商品
        EUtill.throwIf(totalQuantity == 0, ResultCode.ORDER_NOT_FOUND.getCode(),
                "订单存在但无商品");

        log.info("统计完成, 订单数量: {}, 商品总数量: {}", orders.size(), totalQuantity);

        return totalQuantity;
    }
}
