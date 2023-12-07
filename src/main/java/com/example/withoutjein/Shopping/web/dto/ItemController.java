package com.example.withoutjein.Shopping.web.dto;

import com.example.withoutjein.Shopping.config.auth.PrincipalDetails;
import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @GetMapping("/item/write")
    public String itemWriteForm(){
        return "/user/itemwrite";
    }


    @PostMapping("/item/writting")
    public String itemWritting(Item item, Model model, MultipartFile file,@AuthenticationPrincipal PrincipalDetails principalDetails)throws Exception{
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            item.setUser(principalDetails.getUser());
            itemService.save(item, file);
            return "redirect:/main";
        }else{
            return "redirect:/main";
        }
    }


    @GetMapping("/item/view/{id}")
    public String itemView(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            model.addAttribute("item", itemService.itemView(id));
            return "/none/itemview";
        }else{
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("item", itemService.itemView(id));
            return "/user/itemview";
        }
    }


    @GetMapping("/item/modify/{id}")
    public String itemModify(@PathVariable("id") Long id, Model model){
        model.addAttribute("item",itemService.itemView(id));

        return "/user/itemmodify";
    }


    @PostMapping("/item/update/{id}")
    public String itemUpdate(@PathVariable("id") Long id, Item item, MultipartFile file) throws Exception{
        itemService.itemModify(item,id,file);

        return "redirect:/main";
    }


    @GetMapping("/item/delete")
    public String itemDelete(Long id){
        itemService.itemDelete(id);

        return "redirect:/main";
    }

}
