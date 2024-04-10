package com.onlinefoodordering.controller;

import com.onlinefoodordering.model.IngredientCategory;
import com.onlinefoodordering.model.IngredientsItem;
import com.onlinefoodordering.request.IngredientCategoryRequest;
import com.onlinefoodordering.request.IngredientRequest;
import com.onlinefoodordering.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredient")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;
    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory
            (@RequestBody IngredientCategoryRequest ingredientCategoryRequest) throws Exception {
        IngredientCategory ingredientCategory = ingredientService.createIngredientCategory(ingredientCategoryRequest.getName(),
                ingredientCategoryRequest.getRestaurantId());
        return new ResponseEntity<>(ingredientCategory, HttpStatus.CREATED);

    }
    @PostMapping
    public ResponseEntity<IngredientsItem> createIngredientItem
            (@RequestBody IngredientRequest request) throws Exception {
        IngredientsItem ingredientItem = ingredientService.createIngredientItem(request.getRestaurantId(), request.getName(),
                request.getCategoryId());
        return new ResponseEntity<>(ingredientItem, HttpStatus.CREATED);

    }
    @PutMapping({"/{id}/stock"})
    public ResponseEntity<IngredientsItem> updateIngredientItemStock
            (@PathVariable Long id) throws Exception {
        IngredientsItem ingredientItem = ingredientService.updateStock(id);
        return new ResponseEntity<>(ingredientItem, HttpStatus.OK);

    }
    @GetMapping({"/restaurant/{id}"})
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredients
            (@PathVariable Long id) throws Exception {
        List<IngredientsItem> ingredientItems = ingredientService.findRestaurantIngredients(id);
        return new ResponseEntity<>(ingredientItems, HttpStatus.OK);

    }
    @GetMapping({"/restaurant/{id}/category"})
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategories
            (@PathVariable Long id) throws Exception {
        List<IngredientCategory> ingredientCategory = ingredientService.findIngredientCategoryByRestaurantId(id);
        return new ResponseEntity<>(ingredientCategory, HttpStatus.OK);

    }
}
