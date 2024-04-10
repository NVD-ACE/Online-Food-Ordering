package com.onlinefoodordering.controller;

import com.onlinefoodordering.model.Cart;
import com.onlinefoodordering.model.CartItem;
import com.onlinefoodordering.model.User;
import com.onlinefoodordering.request.AddCartItemRequest;
import com.onlinefoodordering.request.UpdateCartRequest;
import com.onlinefoodordering.service.CartService;
import com.onlinefoodordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // This means URL's start with /api (after Application path)
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemsToCart(@RequestBody AddCartItemRequest request,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        CartItem cartItem = cartService.addCartItem(request, jwt);
//        return ResponseEntity.ok(cartItem);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(@RequestBody UpdateCartRequest request,
                                                        @RequestHeader("Authorization") String jwt) throws Exception {
        CartItem cartItem = cartService.updateCartItem(request.getCartItemId(), request.getQuantity());
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
//    @PathVariable("id") Long cartItemId
    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        Cart cart = cartService.removeItemFromCart(id, jwt);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.clearCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findCartByUserId(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
