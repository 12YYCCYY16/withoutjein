package com.example.withoutjein.Shopping.web.dto;

import com.example.withoutjein.Shopping.config.auth.PrincipalDetails;
import com.example.withoutjein.Shopping.domain.cart.Cart;
import com.example.withoutjein.Shopping.domain.cart_item.Cart_item;
import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.domain.order.Order;
import com.example.withoutjein.Shopping.domain.order_item.Order_item;
import com.example.withoutjein.Shopping.domain.user.User;
import com.example.withoutjein.Shopping.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserPageController {
    private final UserPageService userPageService;
    private final ItemService itemService;
    private final CartService cartService;
    private final AuthService authService;
    private final OrderService orderService;


    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() == id){
            model.addAttribute("user",userPageService.findUser(id));
            return "/user/mypage";
        }else{
            return "redirect:/main";
        }
    }



    @GetMapping("/user/{id}/item")
    public String myItemPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User tempUser = userPageService.findUser(id);

        model.addAttribute("user",userPageService.findUser(id));
        model.addAttribute("itemList",itemService.userItemView(tempUser));
        return "/user/myitem";
    }


    @GetMapping("/user/{id}/cart")
    public String myCartPage(@PathVariable("id") Integer id,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails){

        if(principalDetails.getUser().getId() == id){

            Cart cart = principalDetails.getUser().getCart();


            List<Cart_item> cart_items = cartService.userCartView(cart);

            int totalPrice = 0;
            for(Cart_item cart_item : cart_items){
                totalPrice += ( cart_item.getItem().getPrice() * cart_item.getCount());
            }

            model.addAttribute("cart_itemList",cart_items);
            model.addAttribute("totalPrice",totalPrice);
            model.addAttribute("user",userPageService.findUser(id));

            return "/cart/cart";
        }else{
            return "redirect:/main";
        }
    }


    @PostMapping("/user/{id}/cart/{itemId}")
    public String myCartAdd(@PathVariable("id") Integer id,@PathVariable("itemId") Long itemId,int count){
        User user = userPageService.findUser(id);
        Item item = itemService.itemView(itemId);

        cartService.addCart(user,item,count);

        return "redirect:/item/view/{itemId}";
    }


    @GetMapping("/user/{id}/cart/{cart_itemId}/delete")
    public String myCartDelete(@PathVariable("id") Integer id, @PathVariable("cart_itemId") int cart_itemId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = userPageService.findUser(id);
        user.getCart().setCount(user.getCart().getCount() - 1);
        cartService.cartItemDelete(cart_itemId);

        return "redirect:/user/{id}/cart";
    }


    @Transactional
    @PostMapping("/user/{id}/cart/checkout")
    public String myCartPayment(@PathVariable("id") Integer id, Model model){
        User user = userPageService.findUser(id);
        cartService.cartPayment(id);
        cartService.cartDelete(id);
        return "redirect:/user/{id}/order";
    }


    @Transactional
    @GetMapping("/user/{id}/order")
    public String myOrderPage(@PathVariable("id") Integer id,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails){

        if(principalDetails.getUser().getId() == id){

            User user = userPageService.findUser(id);
            List<Order> orderList = user.getOrders();

            model.addAttribute("orderList",orderList);
            model.addAttribute("user",user);

            return "/user/order";
        }else{
            return "redirect:/main";
        }
    }


    @Transactional
    @GetMapping("/user/{id}/sale")
    public String mySalePage(@PathVariable("id")Integer id,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() == id){

            User seller = userPageService.findUser(id);

            List<Order> orderList = orderService.orderList();
            List<Order> mySaleList = new ArrayList<>();

            for(Order order : orderList){
                List<Order_item> orderItemList = order.getOrder_items();

                for(Order_item order_item : orderItemList){
                    if(seller == order_item.getItem().getUser()){
                        mySaleList.add(order);
                        break;
                    }
                }
            }

            model.addAttribute("saleList",mySaleList);
            model.addAttribute("user",seller);

            return "user/sale";
        }else{
            return "redirect:/main";
        }
    }


    @Transactional
    @GetMapping("/user/{id}/cash")
    public String myCash(@PathVariable("id") Integer id,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails){

        if(principalDetails.getUser().getId() == id){

            User user = userPageService.findUser(id);
            model.addAttribute("user",user);

            return "/user/cash";
        }else{
            return "redirect:/main";
        }
    }


    @GetMapping("/user/charge/point")
    public String myCashPro(int amount,@AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = userPageService.findUser(principalDetails.getUser().getId());
        userPageService.chargePoint(user.getId(),amount);
        return "redirect:/main";
    }


    @GetMapping("/user/{id}/admin")
    public String adminPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User admin = userPageService.findUser(id);

        if(admin.getRole().equals("ROLE_ADMIN")){
            List<User> userList = userPageService.userList();
            model.addAttribute("user",admin);
            model.addAttribute("userList",userList);
            return "/user/adminpage";
        }else{
            return "redirect:/main";
        }
    }


    @PostMapping("/user/change/{id}")
    public String userChange(@PathVariable("id") Integer id, User user){
        userPageService.userUpdate(id,user);

        return "redirect:/main";
    }
}
