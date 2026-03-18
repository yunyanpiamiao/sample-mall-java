package com.example.mall.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderStatisticsQuery {
    private String orderSn;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long userId;
}