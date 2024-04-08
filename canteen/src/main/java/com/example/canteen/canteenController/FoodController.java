package com.example.canteen.canteenController;

import com.example.canteen.canteenInterface.ConnectionInterface;
import com.example.canteen.canteenInterface.FoodInterface;
import com.example.canteen.model.FoodEntity;
import com.example.canteen.module.CanteenModule;
import com.example.canteen.service.FoodServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

@RestController
public class FoodController implements FoodInterface {
    private final Injector injector = Guice.createInjector(new CanteenModule());
    private final ConnectionInterface request = injector.getInstance(ConnectionInterface.class);
    @Inject
    private final DynamoDbClient dynamoDbClient = request.getDdbClient();

    @Override
    public String addNewFood(@RequestBody final FoodEntity foodEntity){
        return FoodServiceImpl.addNewFood(dynamoDbClient, foodEntity);
    }

    @Override
    public List<FoodEntity> getAllFoodItems(){
        return FoodServiceImpl.getAllFoodItems(dynamoDbClient);
    }

    @Override
    public String editFoodDetail(final String productId, final FoodEntity foodEntity) {
        return FoodServiceImpl.editFoodDetail(dynamoDbClient, productId, foodEntity);
    }

    @Override
    public List<FoodEntity> getFoodDetailsOfType(final String foodType) {
        return FoodServiceImpl.getFoodDetailsOfType(dynamoDbClient, foodType);
    }

    @Override
    public String deleteFoodItem(String productId) {
        return FoodServiceImpl.deleteFoodItem(dynamoDbClient, productId);
    }

    @Override
    public FoodEntity getDetailOfSingleFood(final String productId){
        return FoodServiceImpl.getDetailOfSingleFood(dynamoDbClient, productId);
    }
}
