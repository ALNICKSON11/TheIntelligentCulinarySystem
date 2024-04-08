package com.example.canteen.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodEntity {
    private String productId;
    private String productName;
    private String thumbnailImage;
    private Integer productPrice;
    private String foodType;
    private String firstImageLink;
    private String secondImageLink;
    private String thirdImageLink;

    public FoodEntity(String productId, String productName, String thumbnailImage, int productPrice, String foodType) {
        this.productId = productId;
        this.productName = productName;
        this.thumbnailImage = thumbnailImage;
        this.productPrice = productPrice;
        this.foodType = foodType;
    }
}
