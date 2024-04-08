package com.example.canteen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderEntity {
    private Integer orderId;
    private String userEmailId;
    List<OrderProductEntity> orderProductEntityList;
    private Integer totalPrice;
    private String paymentId;

    public OrderEntity(int orderId, String userEmailId, Integer totalPrice, String paymentId) {
        this.orderId = orderId;
        this.userEmailId = userEmailId;
        this.totalPrice = totalPrice;
        this.paymentId = paymentId;
    }
}
