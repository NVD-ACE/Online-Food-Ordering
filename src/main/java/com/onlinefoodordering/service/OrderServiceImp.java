package com.onlinefoodordering.service;

import com.onlinefoodordering.model.*;
import com.onlinefoodordering.repository.AddressRepository;
import com.onlinefoodordering.repository.OrderItemRepository;
import com.onlinefoodordering.repository.OrderRepository;
import com.onlinefoodordering.repository.UserRepository;
import com.onlinefoodordering.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;
    @Override
    public Order createOrder(OrderRequest orderRequest, User user) throws Exception {
        Address address = orderRequest.getDeliveryAddress();
        Address savedAddress = addressRepository.save(address);
        if(!user.getAddresses().contains(savedAddress)){
            user.getAddresses().add(savedAddress);
            userRepository.save(user);
        }
        Restaurant restaurant = restaurantService.findRestaurantById(orderRequest.getRestaurantId());
        Order order = new Order();
        order.setOrderStatus("PLACED");
        order.setDeliveryAddress(savedAddress);
        order.setCustomer(user);
        order.setCreatedAt(new Date());
        order.setRestaurant(restaurant);
        Cart cart = cartService.findCartByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem: cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }
        Long totalPrice = cartService.calculateCartTotals(cart);
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        restaurant.getOrders().add(savedOrder);
        return order;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if(orderStatus.equals("CANCELLED") ||
                orderStatus.equals("DELIVERED") ||
                orderStatus.equals("PLACED") ||
                orderStatus.equals("PREPARING") ||
                orderStatus.equals("READY") ||
                orderStatus.equals("OUT_FOR_DELIVERY") ||
                orderStatus.equals("COMPLETED")){
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("Please provide a valid order status");
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getUserOrders(Long userId) throws Exception {

        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if(orderStatus != null){

//           orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
            orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).toList();
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty()){
            throw new Exception("Order not found");
        }
        return order.get();
    }
}
