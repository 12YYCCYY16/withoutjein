package com.example.withoutjein.Shopping.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByUserId(int userId);
}
