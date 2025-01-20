package com.example.canteen.canteenInterface;

import com.example.canteen.model.FoodEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/food")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public interface FoodInterface {
    /**
     * This method is used to add new food item
     */
    @PostMapping("/add")
    public String addNewFood(@RequestBody final FoodEntity foodEntity);

    /**
     * This method is used to retrieve all food items from the food table.
     */
    @GetMapping("/")
    public List<FoodEntity> getAllFoodItems();

    /**
     * This method is used to edit the details of a particular food item.
     *
     * @return String to mention the success or failure of the operation.
     */
    @PutMapping("/edit/{productId}")
    public String editFoodDetail(@PathVariable final Integer productId, @RequestBody final FoodEntity foodEntity);

    /**
     * This method is used to retrieve the details of a particular food item.
     *
     * @return FoodEntity object containing the details of the food item.
     */
    @GetMapping("/{productId}")
    public FoodEntity getDetailOfSingleFood(@PathVariable final Integer productId);

    /**
     * This method is used to retrieve the food items of a particular type.
     *
     * @return List of food items of a particular type.
     */
    @GetMapping("/type/{foodType}")
    public List<FoodEntity> getFoodDetailsOfType(@PathVariable final String foodType);

    /**
     * This method is used to delete a particular food item.
     *
     * @return String to mention the success or failure of the operation.
     */
    @DeleteMapping("/delete/{productId}")
    public String deleteFoodItem(@PathVariable final Integer productId);
}
