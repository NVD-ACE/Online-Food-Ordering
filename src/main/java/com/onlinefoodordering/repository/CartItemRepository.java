package com.onlinefoodordering.repository;

import com.onlinefoodordering.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
