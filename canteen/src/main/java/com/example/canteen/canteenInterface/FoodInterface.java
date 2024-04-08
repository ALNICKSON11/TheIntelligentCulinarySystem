package com.example.canteen.canteenInterface;

import com.example.canteen.model.FoodEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FoodInterface {
    /**
     * Add new food.
     */
    @PostMapping("/add/food")
    public String addNewFood(@RequestBody final FoodEntity foodEntity);

    /**
     * get all food items
     */
    @GetMapping("/get/foods")
    public List<FoodEntity> getAllFoodItems();

    /**
     *  Edit food detail
     */
    @PutMapping("/food/{productId}")
    public String editFoodDetail(@PathVariable final String productId, @RequestBody final FoodEntity foodEntity);

    /**
     * Get food details of a single food
     */
    @GetMapping("/get/food/{productId}")
    public FoodEntity getDetailOfSingleFood(@PathVariable final String productId);

    /**
     * Get food details of particular type
     */
    @GetMapping("/food/{foodType}")
    public List<FoodEntity> getFoodDetailsOfType(@PathVariable final String foodType);

    /**
     *  Deleting a particular food item
     */
    @DeleteMapping("/delete/{productId}")
    public String deleteFoodItem(@PathVariable final String productId);
}
