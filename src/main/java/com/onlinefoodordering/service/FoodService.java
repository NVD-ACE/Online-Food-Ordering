package com.onlinefoodordering.service;

import com.onlinefoodordering.model.Category;
import com.onlinefoodordering.model.Food;
import com.onlinefoodordering.model.Restaurant;
import com.onlinefoodordering.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant);
    void deleteFood(Long foodId) throws Exception;
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian,
                                         boolean isNonVeg,
                                         boolean isSeasonal,
                                         String foodCategory);
    public List<Food> searchFood(String query);
    public Food findFoodById(Long foodId) throws Exception;

    public Food updateAvailabilityStatus(Long foodId) throws Exception;
}
