package com.example.withoutjein.Shopping.domain.cart;

import com.example.withoutjein.Shopping.domain.cart_item.Cart_item;
import com.example.withoutjein.Shopping.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int count;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    User user;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Cart_item> cart_items = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

    @PrePersist
    public void createDate(){
        this.createDate = LocalDate.now();
    }

    public static Cart createCart(User user){
        Cart cart = new Cart();
        cart.user = user;
        cart.count = 0;

        return cart;
    }
}
