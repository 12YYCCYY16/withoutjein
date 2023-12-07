package com.example.withoutjein.Shopping.domain.order_item;

import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.domain.order.Order;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Order_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int count;

    private int price;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    public static Order_item createOrderItem(Item item, int count){

        Order_item order_item = new Order_item();
        order_item.setItem(item);
        order_item.setCount(count);
        order_item.setPrice(item.getPrice());

        return order_item;
    }
}
