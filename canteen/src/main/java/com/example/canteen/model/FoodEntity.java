package com.example.canteen.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This class represents the food entity.
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodEntity {
    /**
     * An unique id for all the products
     */
    private Integer productId;

    /**
     * Name of the product
     */
    private String productName;

    /**
     * The image of the product
     */
    private String thumbnailImage;

    /**
     * The price fo the product
     */
    private Integer productPrice;

    /**
     * The type in which the product falls in.
     */
    private String foodType;

    /**
     * The link of the first image
     */
    private String firstImageLink;

    /**
     * The link of the second image
     */
    private String secondImageLink;

    /**
     * The link of the third image
     */
    private String thirdImageLink;

    /**
     * Constructor for the FoodEntity class without image links.
     */
    public FoodEntity(Integer productId, String productName, String thumbnailImage, Integer productPrice, String foodType) {
        this.productId = productId;
        this.productName = productName;
        this.thumbnailImage = thumbnailImage;
        this.productPrice = productPrice;
        this.foodType = foodType;
    }
}
