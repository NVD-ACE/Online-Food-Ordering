package com.onlinefoodordering.request;

import lombok.Data;

@Data
public class IngredientCategoryRequest {
    private String name;
    private Long RestaurantId;
}
