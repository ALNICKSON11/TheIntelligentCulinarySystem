package com.example.canteen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProductEntity {

    /**
     * Unique Id for all the products
     */
    private String productId;

    /**
     * This is the name of the product that is added to the cart.
     */
    private String productName;

    /**
     * This is the number of products that are added to the cart.
     */
    private Integer productCount;

    /**
     * This is the price of the product that is added to the cart.
     */
    private Integer productPrice;
}
