package com.example.withoutjein.Shopping.web.dto;

import com.example.withoutjein.Shopping.domain.order.Order;
import com.example.withoutjein.Shopping.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/update/{id}")
    public String orderUpdate(@PathVariable("id") Integer id, Order order){
        orderService.orderUpdate(id,order);

        return "redirect:/main";
    }
}
