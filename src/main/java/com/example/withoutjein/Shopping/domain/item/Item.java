package com.example.withoutjein.Shopping.domain.item;

import com.example.withoutjein.Shopping.domain.cart_item.Cart_item;
import com.example.withoutjein.Shopping.domain.order_item.Order_item;
import com.example.withoutjein.Shopping.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity

@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int price;

    private int stock;

    private boolean isSoldout;

    private int count;

    private String text;

    private String filename;
    private String filepath;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item")
    private List<Cart_item> cart_items = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Order_item> order_items = new ArrayList<>();

    public void changeStock(int count){
        this.stock = this.stock + count;
    }
}
