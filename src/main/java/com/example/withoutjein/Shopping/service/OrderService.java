package com.example.withoutjein.Shopping.service;

import com.example.withoutjein.Shopping.domain.cart_item.Cart_item;
import com.example.withoutjein.Shopping.domain.order.Order;
import com.example.withoutjein.Shopping.domain.order.OrderRepository;
import com.example.withoutjein.Shopping.domain.order_item.Order_item;
import com.example.withoutjein.Shopping.domain.sale.SaleRepository;
import com.example.withoutjein.Shopping.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final SaleRepository saleRepository;

    public void createOrder(User user){
        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);
    }

    public void order(User user, List<Cart_item> cart_items){
        List<Order_item> order_items = new ArrayList<>();

        for(Cart_item cart_item : cart_items){
            Order_item order_item = Order_item.createOrderItem(cart_item.getItem(),cart_item.getCount());
            order_items.add(order_item);
        }

        Order order = Order.createOrder(user,order_items);
        order.setPrice(order.getTotalPrice());
        orderRepository.save(order);
    }


    public List<Order> orderList(){
        return orderRepository.findAll();
    }


    public Order orderView(Integer id){
        return orderRepository.findById(id).get();
    }


    public void orderUpdate(Integer id, Order order){
        Order tempOrder = orderRepository.findById(id).get();
        tempOrder.setStatus(order.getStatus());

        orderRepository.save(tempOrder);
    }


}
