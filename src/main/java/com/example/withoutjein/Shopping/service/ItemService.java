package com.example.withoutjein.Shopping.service;

import com.example.withoutjein.Shopping.domain.item.Item;
import com.example.withoutjein.Shopping.domain.item.ItemRepository;
import com.example.withoutjein.Shopping.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;


    public void save(Item item, MultipartFile file) throws Exception{

        if(file != null){
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath,fileName);
            file.transferTo(saveFile);

            item.setFilename(fileName);
            item.setFilepath("/files/" + fileName);
        }else{
            item.setFilepath("https://dummyimage.com/450x300/dee2e6/6c757d.jpg");
        }

        item.setCount(item.getStock());
        item.setSoldout(true);
        itemRepository.save(item);
    }


    public List<Item> itemList(){
        return itemRepository.findAll();
    }


    public Item itemView(Long id){
        return itemRepository.findById(id).get();
    }


    public List<Item> userItemView(User user){
        List<Item> itemList = itemRepository.findAll();
        List<Item> tempList = new ArrayList<>();

        for(Item item : itemList){
            if(item.getUser() == user){
                tempList.add(item);
            }
        }

        return tempList;
    }


    public void itemModify(Item item, Long id, MultipartFile file)throws Exception{
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath,fileName);
        file.transferTo(saveFile);

        Item tempItem = itemRepository.findItemById(id);
        tempItem.setName(item.getName());
        tempItem.setPrice(item.getPrice());
        tempItem.setStock(item.getStock());


        tempItem.setText(item.getText());
        tempItem.setFilename(fileName);
        tempItem.setFilepath("/files/" + fileName);

        itemRepository.save(tempItem);
    }


    public void itemDelete(Long id){
        itemRepository.deleteById(id);
    }

}
