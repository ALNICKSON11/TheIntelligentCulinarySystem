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
    private String productId;
    private String productName;
    private Integer productCount;
    private Integer productPrice;
}
