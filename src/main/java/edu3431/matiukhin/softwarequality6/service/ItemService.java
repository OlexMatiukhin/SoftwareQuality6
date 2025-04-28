package edu3431.matiukhin.softwarequality6.service;/*
@author sasha
@project SoftwareQuality5
@class ItemService
@version 1.0.0
@since 14.04.2025 - 15 - 29
*/



import edu3431.matiukhin.softwarequality6.dto.ItemDTO;
import edu3431.matiukhin.softwarequality6.model.Item;
import edu3431.matiukhin.softwarequality6.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    // Optional hardcoded items for testing
    public List<Item> items = new ArrayList();
    {
        items.add(new Item("1", "Smartphones and mobile phones", "Smartphone", "Redmi Note 5A", 3333, "00001", "Made in China."));
        items.add(new Item("2", "Furniture", "Office chair", "Art Metal Furniture A-36 black", 4444, "00002", "Just usual office chair"));
        items.add(new Item("3", "Large household appliances", "Fridges", "MPM MPM-46-CJ-01", 3333, "00003", "Refrigerator with freezer compartment"));
    }

    @PostConstruct
    public void init() {
        this.itemRepository.deleteAll();
        this.itemRepository.saveAll(this.items);
    }

    public List<ItemDTO> getAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ItemDTO getById(String id) {
        Item item = this.itemRepository.findById(id).orElse(null);
        return item != null ? toDto(item) : null;
    }

    public ItemDTO createItem(ItemDTO itemDTO) {
        Item itemFromDTO = fromDto(itemDTO);
        Item savedItem = this.itemRepository.save(itemFromDTO);
        return toDto(savedItem);  // Return the saved item as DTO
    }

    public ItemDTO updateItem(ItemDTO itemDTO) {
        Item itemFromDTO = fromDto(itemDTO);
        Item savedItem = this.itemRepository.save(itemFromDTO);
        return toDto(savedItem);  // Return the saved item as DTO
    }

    public void deleteById(String id) {
        this.itemRepository.deleteById(id);
    }

    public Item fromDto(ItemDTO dto) {
        return new Item(dto.id(), dto.category(), dto.type(), dto.name(), dto.price(), dto.code(), dto.description());
    }

    public ItemDTO toDto(Item item) {
        return new ItemDTO(item.getId(), item.getCategory(), item.getType(), item.getName(), item.getPrice(), item.getCode(), item.getDescription());
    }
}
