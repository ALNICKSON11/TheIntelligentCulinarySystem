package com.example.canteen.canteenInterface;

import com.example.canteen.model.OrderEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderInterface {
    /**
     * Add new order detail
     */
    @PostMapping("/order")
    public String addNewOrderDetail(@RequestBody OrderEntity orderEntity);

    /**
     * get all order details
     */
    @GetMapping("/get/orders")
    public List<OrderEntity> getAllOrderDetails();

    /**
     * get all order details of a particular user
     */
    @GetMapping("/order/{userEmailId}")
    public List<OrderEntity> getOrderDetailsForUser(@PathVariable String userEmailId);
}
