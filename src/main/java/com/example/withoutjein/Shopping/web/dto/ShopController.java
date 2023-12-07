package com.example.withoutjein.Shopping.web.dto;

import com.example.withoutjein.Shopping.config.auth.PrincipalDetails;
import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.domain.user.User;
import com.example.withoutjein.Shopping.service.ItemService;
import com.example.withoutjein.Shopping.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ShopController {
    private final UserPageService userPageService;
    private final ItemService itemService;


    @GetMapping("/")
    public String home(Model model){
        List<Item> itemList = itemService.itemList();
        model.addAttribute("itemlist",itemList);
        return "/none/main";
    }


    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        List<Item> itemList = itemService.itemList();

        if (principalDetails != null && principalDetails.getUser() != null) {
            User user = userPageService.findUser(principalDetails.getUser().getId());
            model.addAttribute("user", user);
        }

        model.addAttribute("itemlist", itemList);
        return "/user/main";
    }



}
