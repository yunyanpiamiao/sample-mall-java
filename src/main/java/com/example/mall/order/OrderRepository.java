package com.example.mall.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.userId = :userId " +
           "AND (:orderSn IS NULL OR o.orderSn LIKE %:orderSn%) " +
           "AND (:status IS NULL OR o.status = :status) " +
           "AND (:startTime IS NULL OR o.createdAt >= :startTime) " +
           "AND (:endTime IS NULL OR o.createdAt <= :endTime)")
    List<Order> findByConditions(@Param("userId") Long userId,
                                 @Param("orderSn") String orderSn,
                                 @Param("status") Integer status,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);
}
