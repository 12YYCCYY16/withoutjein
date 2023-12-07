package com.example.withoutjein.Shopping.service;

import com.example.withoutjein.Shopping.domain.cart.Cart;
import com.example.withoutjein.Shopping.domain.cart.CartRepository;
import com.example.withoutjein.Shopping.domain.cart_item.Cart_item;
import com.example.withoutjein.Shopping.domain.cart_item.Cart_itemRepository;
import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.domain.user.User;
import com.example.withoutjein.Shopping.domain.user.UserRepository;
import com.example.withoutjein.Shopping.exception.OutOfMoneyException;
import com.example.withoutjein.Shopping.exception.OutOfStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final Cart_itemRepository cart_itemRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public void createCart(User user){
        Cart cart = Cart.createCart(user);
        cartRepository.save(cart);
    }


    @Transactional
    public void addCart(User user, Item item, int count){

        Cart cart = cartRepository.findByUserId(user.getId());


        if(cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }


        Cart_item cart_item = cart_itemRepository.findByCartIdAndItemId(cart.getId(),item.getId());


        if(cart_item == null){
            cart_item = Cart_item.createCartItem(cart,item,count);
            cart_itemRepository.save(cart_item);
            cart.setCount(cart.getCount() + 1);
        }else{

            cart_item.addCount(count);
        }

    }


    public List<Cart_item> userCartView(Cart cart){
        List<Cart_item> cart_items = cart_itemRepository.findAll();
        List<Cart_item> user_items = new ArrayList<>();

        for(Cart_item cart_item : cart_items){
            if(cart_item.getCart().getId() == cart.getId()){
                user_items.add(cart_item);
            }
        }

        return user_items;
    }


    public void cartItemDelete(int id){
        cart_itemRepository.deleteById(id);
    }


    public void cartDelete(int id){
        List<Cart_item> cart_items = cart_itemRepository.findAll();


        for(Cart_item cart_item : cart_items){
            if(cart_item.getCart().getUser().getId() == id){
                cart_item.getCart().setCount(cart_item.getCart().getCount() - 1);
                cart_itemRepository.deleteById(cart_item.getId());
            }
        }
    }


    @Transactional
    public void cartPayment(int id){
        List<Cart_item> cart_items = cart_itemRepository.findAll();
        List<Cart_item> userCart_items = new ArrayList<>();
        User buyer = userRepository.findById(id).get();


        for(Cart_item cart_item : cart_items){
            if(cart_item.getCart().getUser().getId() == buyer.getId()){
                userCart_items.add(cart_item);
            }
        }

        orderService.order(buyer,userCart_items);



        for(Cart_item cart_item : userCart_items){

            int stock = cart_item.getItem().getStock();
            if(stock < cart_item.getCount()){
                throw new OutOfStockException("상품의 재고가 부족합니다. ( 현재 재고 수량 : " + stock + ")");
            }
            stock = stock - cart_item.getCount();
            cart_item.getItem().setStock(stock);


            User seller = cart_item.getItem().getUser();
            int cash = cart_item.getItem().getPrice();

            if(buyer.getMoney() < cash){
                throw new OutOfMoneyException("보유 머니가 부족합니다 . ( 현재 보유 머니 : " + buyer.getMoney() + ")");
            }
            buyer.setMoney(cash * -1);
            seller.setMoney(cash);
        }
    }

}
