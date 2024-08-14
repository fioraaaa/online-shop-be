package com.project.online_shop_be.service;

import com.project.online_shop_be.dto.CustomerResponseDto;
import com.project.online_shop_be.dto.ItemDto;
import com.project.online_shop_be.model.Customer;
import com.project.online_shop_be.model.Item;
import com.project.online_shop_be.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(ItemDto itemDto) {
        Item item = new Item();
        item.setItemsName(itemDto.getItemsName());
        item.setItemsCode(itemDto.getItemsCode());
        item.setStock(itemDto.getStock());
        item.setPrice(itemDto.getPrice());
        item.setIsAvailable(itemDto.getIsAvailable());
        item.setLastReStock(itemDto.getLastReStock());
        return itemRepository.save(item);
    }

    public Item updateItem(Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item tidak ditemukan"));

        System.out.println("Updating Item with details: " + itemDto);

        if (itemDto.getItemsName() != null) {
            item.setItemsName(itemDto.getItemsName());
        }
        if (itemDto.getItemsCode() != null) {
            item.setItemsCode(itemDto.getItemsCode());
        }
        if (itemDto.getStock() != null) {
            item.setStock(itemDto.getStock());
        }
        if (itemDto.getPrice() != null) {
            item.setPrice(itemDto.getPrice());
        }
        if (itemDto.getIsAvailable() != null) {
            item.setIsAvailable(itemDto.getIsAvailable());
        }
        if (itemDto.getLastReStock() != null) {
            item.setLastReStock(itemDto.getLastReStock());
        }
        return itemRepository.save(item);
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public Item getItemById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        return optionalItem.orElse(null);
    }

    public Page<Item> getAllItems(int page, int size) {
        return itemRepository.findAll(PageRequest.of(page, size));
    }

}
