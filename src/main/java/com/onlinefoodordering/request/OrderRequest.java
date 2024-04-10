package com.onlinefoodordering.request;

import com.onlinefoodordering.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long restaurantId;
    private Address deliveryAddress;
}
