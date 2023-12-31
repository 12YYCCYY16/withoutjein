package com.example.withoutjein.Shopping.domain.order;

import com.example.withoutjein.Shopping.domain.order_item.Order_item;
import com.example.withoutjein.Shopping.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status;

    private int price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order_item> order_items = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createDate;

    public void addOrderItem(Order_item order_item){
        order_items.add(order_item);
        order_item.setOrder(this);
    }

    public static Order createOrder(User user, List<Order_item> orderItemList){
        Order order = new Order();
        order.setUser(user);
        for(Order_item order_item : orderItemList){
            order.addOrderItem(order_item);
        }
        order.setStatus("주문 완료");
        order.setCreateDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;

        for(Order_item order_item : order_items){
            totalPrice += (order_item.getPrice() * order_item.getCount());
        }

        return totalPrice;
    }
}
