package com.example.canteen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class represents an order entity.
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderEntity {
    /**
     * The Unique Id for each order.
     */
    private Integer orderId;

    /**
     * The email Id of the user who placed the order.
     */
    private String userEmailId;

    /**
     * The list of products in the order.
     */
    List<OrderProductEntity> orderProductEntityList;

    /**
     * The total price of the order.
     */
    private Integer totalPrice;

    /**
     * The payment Id of the order.
     */
    private String paymentId;

    /**
     * Constructor for OrderEntity class without orderProductEntityList.
     */
    public OrderEntity(int orderId, String userEmailId, Integer totalPrice, String paymentId) {
        this.orderId = orderId;
        this.userEmailId = userEmailId;
        this.totalPrice = totalPrice;
        this.paymentId = paymentId;
    }
}
