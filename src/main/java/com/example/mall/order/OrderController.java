package com.example.mall.order;

import com.example.mall.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/api/orders", "/order"})
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> listAll() {
        return orderService.listAll();
    }

    // 对齐 mall-admin：/order/list 查询订单列表
    @GetMapping("/list")
    public List<Order> list() {
        return orderService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        Order created = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order) {
        Order updated = orderService.update(id, order);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/statistics/product-quantity")
    public ApiResponse<Integer> countProductQuantity(@RequestBody OrderStatisticsQuery query) {
        Integer totalQuantity = orderService.countProductQuantityByConditions(query);
        return ApiResponse.success(totalQuantity);
    }
}
