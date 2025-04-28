package edu3431.matiukhin.softwarequality6.controller;


import edu3431.matiukhin.softwarequality6.dto.ItemDTO;
import edu3431.matiukhin.softwarequality6.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/v1/items"})
public class ItemRestController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDTO> getAllItems() {
        return this.itemService.getAll();
    }

    @GetMapping({"/{id}"})
    public ItemDTO getItemById(@PathVariable String id) {
        return this.itemService.getById(id);
    }

    @PostMapping
    public ItemDTO createItem(@RequestBody ItemDTO item) {
        return this.itemService.createItem(item);
    }

    @PutMapping
    public ItemDTO updateItem(@RequestBody ItemDTO item) {
        return this.itemService.updateItem(item);
    }

    @DeleteMapping({"/{id}"})
    public void deleteItem(@PathVariable String id) {
        this.itemService.deleteById(id);
    }


}
