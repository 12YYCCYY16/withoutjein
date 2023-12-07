package com.example.withoutjein.Shopping.domain.user;

import com.example.withoutjein.Shopping.domain.cart.Cart;
import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String email;
    private int money;


    private String addr1;
    private String addr2;
    private String addr3;
    private String address;

    private String phone_number;
    private String grade;
    private String role;

    private LocalDateTime createDate;


    @OneToMany(mappedBy = "user")
    private List<Item> items = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

    public void setMoney(int count){
        this.money = this.money + count;
    }

}