package com.example.withoutjein.Shopping.domain.sale;

import com.example.withoutjein.Shopping.domain.order.Order;
import com.example.withoutjein.Shopping.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    private Order order;

    public Sale addSale(User user, Order order){
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setOrder(order);

        return sale;
    }
}
