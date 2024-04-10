package com.onlinefoodordering.controller;

import com.onlinefoodordering.model.Order;
import com.onlinefoodordering.model.User;
import com.onlinefoodordering.request.OrderRequest;
import com.onlinefoodordering.service.OrderService;
import com.onlinefoodordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @GetMapping("/order/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrderRestaurant(
            @PathVariable Long id,
            @RequestParam(required = false) String status,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getRestaurantOrders(id, status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PutMapping("/order/{id}/{orderStatus}")
    public ResponseEntity <Order> updateOrderStatus(
            @PathVariable Long id,
            @PathVariable String orderStatus,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.updateOrder(id, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
