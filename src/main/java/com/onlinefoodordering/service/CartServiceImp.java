package com.onlinefoodordering.service;

import com.onlinefoodordering.model.Cart;
import com.onlinefoodordering.model.CartItem;
import com.onlinefoodordering.model.Food;
import com.onlinefoodordering.model.User;
import com.onlinefoodordering.repository.CartItemRepository;
import com.onlinefoodordering.repository.CartRepository;
import com.onlinefoodordering.repository.FoodRepository;
import com.onlinefoodordering.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImp implements CartService{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodService foodService;
    @Override
    public CartItem addCartItem(AddCartItemRequest request, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.findFoodById(request.getFoodId());
        Cart cart = cartRepository.findByCustomerId(user.getId());
        for(CartItem cartItem : cart.getItems()) {
            if (cartItem.getFood().equals(food)) {
//                cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
//                return cartItemRepository.save(cartItem);
                int quantity = cartItem.getQuantity() + request.getQuantity();
                return updateCartItem(cartItem.getId(), quantity);
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setFood(food);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setCart(cart);
        cartItem.setIngredients(request.getIngredients());
        cartItem.setTotalPrice(food.getPrice() * request.getQuantity());
//        return cartItemRepository.save(cartItem);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        cart.getItems().add(savedCartItem);
        return savedCartItem;
    }

    @Override
    public CartItem updateCartItem(Long cartItemId, int quantity) throws Exception {
//        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new Exception("Cart item not found"));
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if(cartItemOptional.isEmpty()) {
            throw new Exception("Cart item not found");
        }
        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getFood().getPrice() * quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepository.findByCustomerId(user.getId());
//        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new Exception("Cart item not found"));
//        cartItemRepository.delete(cartItem);
//        cart.getItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if(cartItemOptional.isEmpty()) {
            throw new Exception("Cart item not found");
        }
        CartItem cartItem = cartItemOptional.get();
        cart.getItems().remove(cartItem);
//        cartItemRepository.delete(cartItem);
        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {
        Long total = 0L;
        for(CartItem cartItem : cart.getItems()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if(cartOptional.isEmpty()) {
            throw new Exception("Cart not found");
        }
        return cartOptional.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
//        User user = userService.findUserByJwtToken(jwt);
       Cart cart = cartRepository.findByCustomerId(userId);
       cart.setTotal(calculateCartTotals(cart));
       return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
//        User user = userService.findUserByJwtToken(jwt);
        Cart cart = findCartByUserId(userId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
