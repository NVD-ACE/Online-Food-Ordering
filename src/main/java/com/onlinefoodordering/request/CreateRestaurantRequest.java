package com.onlinefoodordering.request;

import com.onlinefoodordering.model.Address;
import com.onlinefoodordering.model.ContactInformation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateRestaurantRequest {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String OpeningHours;
    private List<String> images;
}
