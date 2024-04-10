package com.onlinefoodordering.service;

import com.onlinefoodordering.model.IngredientCategory;
import com.onlinefoodordering.model.IngredientsItem;
import com.onlinefoodordering.model.Restaurant;
import com.onlinefoodordering.repository.IngredientCategoryRepository;
import com.onlinefoodordering.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class IngredientServiceImp implements IngredientService{
    @Autowired
    private IngredientItemRepository ingredientItemRepository;
    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private RestaurantService restaurantService;
    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setRestaurant(restaurant);
        ingredientCategory.setName(name);
        return ingredientCategoryRepository.save(ingredientCategory);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long ingredientCategoryId) throws Exception {
        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId);
        if(ingredientCategory.isEmpty()) {
            throw new Exception("Ingredient category not found");
        }
        return ingredientCategory.get();
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {
        restaurantService.findRestaurantById(id);
        return ingredientCategoryRepository.findByRestaurantId(id);
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long CategoryId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientsItem ingredientsItem = new IngredientsItem();
        IngredientCategory ingredientCategory = findIngredientCategoryById(CategoryId);
        ingredientsItem.setRestaurant(restaurant);
        ingredientsItem.setName(ingredientName);
        ingredientsItem.setCategory(ingredientCategory);
        IngredientsItem savedIngredientItem = ingredientItemRepository.save(ingredientsItem);
        ingredientCategory.getIngredients().add(savedIngredientItem);
        return savedIngredientItem;

    }

    @Override
    public List<IngredientsItem> findRestaurantIngredients(Long restaurantId) throws Exception {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem updateStock(Long id) throws Exception {
        Optional<IngredientsItem> ingredientItem = ingredientItemRepository.findById(id);
        if(ingredientItem.isEmpty()) {
            throw new Exception("Ingredient item not found");
        }
        IngredientsItem ingredient = ingredientItem.get();
        ingredient.setInStoke(!ingredient.isInStoke());
        return ingredientItemRepository.save(ingredient);
    }
}
