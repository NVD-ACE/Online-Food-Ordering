package com.onlinefoodordering.service;

import com.onlinefoodordering.model.Category;
import com.onlinefoodordering.model.Food;
import com.onlinefoodordering.model.Restaurant;
import com.onlinefoodordering.repository.FoodRepository;
import com.onlinefoodordering.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService{

    @Autowired
    private FoodRepository foodRepository;
    @Override
    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant) {
        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(request.getDescription());
        food.setImages(request.getImages());
        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setIngredients(request.getIngredients());
        food.setSeasonal(request.isSeasonal());
        food.setVegetarian(request.isVegetarian());
        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);
        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setRestaurant(null);
        foodRepository.delete(food);
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian,
                                         boolean isNonVeg,
                                         boolean isSeasonal,
                                         String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
//        if(isVegetarian)
//            foods.removeIf(food -> !food.isVegetarian());
//        if(isNonVeg)
//            foods.removeIf(food -> !food.isVegetarian());
//        if (isSeasonal)
//            foods.removeIf(food -> !food.isSeasonal());
        if(isVegetarian){
            foods = filterByVegetarian(foods, isVegetarian);
        }
        if(isNonVeg){
            foods = filterByNonVeg(foods, isNonVeg);
        }
        if(isSeasonal){
            foods = filterBySeasonal(foods, true); // isVegetarian
        }
        // if(foodCategory != null && !foodCategory.equals("")){ // This is the correct code
        if(foodCategory != null && !foodCategory.isEmpty()){
            foods = filterByCategory(foods, foodCategory);
        }

        return foods;
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
            if(food.getFoodCategory() != null){
                return food.getFoodCategory().getName().equals(foodCategory);
            }
            return false;

        }).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream().filter(food -> food.isSeasonal() == isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food -> !food.isVegetarian()).collect(Collectors.toList());
        //food.isVegetarian() == false
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream().filter(food -> food.isVegetarian() == isVegetarian).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String query) {
        return foodRepository.searchFood(query);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> opt = foodRepository.findById(foodId);
        if(opt.isEmpty()){
            throw new Exception("Food not found with id: " + foodId);
        }
        return opt.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
