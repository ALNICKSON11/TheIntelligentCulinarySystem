package com.example.canteen.service;

import com.example.canteen.model.FoodEntity;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodServiceImpl {
    /**
     * adding new food
     */
    public static String addNewFood(final DynamoDbClient dynamoDbClient, final FoodEntity foodEntity) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("productId", AttributeValue.builder().s(foodEntity.getProductId()).build());

        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("FoodTable")
                .key(key)
                .build();

        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getRequest);
            Map<String, AttributeValue> existingItem = getItemResponse.item();

            if (existingItem != null && !existingItem.isEmpty()) {
                return "Product ID '" + foodEntity.getProductId() + "' already used for "+foodEntity.getProductName();
            } else {
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("productId", AttributeValue.builder().s(foodEntity.getProductId()).build());
                item.put("productName", AttributeValue.builder().s(foodEntity.getProductName()).build());
                item.put("thumbnailImage", AttributeValue.builder().s(foodEntity.getThumbnailImage()).build());
                item.put("productPrice", AttributeValue.builder().n(String.valueOf(foodEntity.getProductPrice())).build());
                item.put("foodType", AttributeValue.builder().s(foodEntity.getFoodType()).build());
                if (foodEntity.getFirstImageLink() != null && !foodEntity.getFirstImageLink().isEmpty()) {
                    item.put("firstImageLink", AttributeValue.builder().s(foodEntity.getFirstImageLink()).build());
                }
                if (foodEntity.getSecondImageLink() != null && !foodEntity.getSecondImageLink().isEmpty()) {
                    item.put("secondImageLink", AttributeValue.builder().s(foodEntity.getSecondImageLink()).build());
                }
                if (foodEntity.getThirdImageLink() != null && !foodEntity.getThirdImageLink().isEmpty()) {
                    item.put("thirdImageLink", AttributeValue.builder().s(foodEntity.getThirdImageLink()).build());
                }

                PutItemRequest request = PutItemRequest.builder()
                        .tableName("FoodTable")
                        .item(item)
                        .build();

                dynamoDbClient.putItem(request);
                return foodEntity.getProductName() + " added successfully";
            }
        } catch (DynamoDbException e) {
            return "Error in adding food item: " + e.getMessage();
        }
    }


    /**
     * getting all food items
     */
    public static List<FoodEntity> getAllFoodItems(final DynamoDbClient dynamoDbClient){
        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("FoodTable")
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<FoodEntity> foodEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()){
                FoodEntity foodEntity = new FoodEntity(
                        item.get("productId").s(),
                        item.get("productName").s(),
                        item.get("thumbnailImage").s(),
                        Integer.parseInt(item.get("productPrice").n()),
                        item.get("foodType").s()
                );

                if (item.containsKey("firstImageLink")) {
                    foodEntity.setFirstImageLink(item.get("firstImageLink").s());
                }

                if (item.containsKey("secondImageLink")) {
                    foodEntity.setSecondImageLink(item.get("secondImageLink").s());
                }

                if (item.containsKey("thirdImageLink")) {
                    foodEntity.setThirdImageLink(item.get("thirdImageLink").s());
                }
                foodEntityList.add(foodEntity);
            }
            return foodEntityList;
        }catch (Exception e){
            System.out.println("Error in retrieving food details"+e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     *Updating the food detail
     */
    public static String editFoodDetail(final DynamoDbClient dynamoDbClient, final String productId, final FoodEntity foodEntity) {
        final Map<String, AttributeValue> updateExpressionValues = new HashMap<>();
        final StringBuilder updateExpressionBuilder = new StringBuilder();

        if (foodEntity.getProductName() != null){
            updateExpressionBuilder.append(", productName = :productName");
            updateExpressionValues.put(":productName", AttributeValue.builder().s(foodEntity.getProductName()).build());
        }

        if(foodEntity.getThumbnailImage() != null){
            updateExpressionBuilder.append(", thumbnailImage = :thumbnailImage");
            updateExpressionValues.put(":thumbnailImage", AttributeValue.builder().s(foodEntity.getThumbnailImage()).build());
        }

        if(foodEntity.getProductPrice() != 0){
            updateExpressionBuilder.append(", productPrice = :productPrice");
            updateExpressionValues.put(":productPrice", AttributeValue.builder().n(String.valueOf(foodEntity.getProductPrice())).build());
        }

        if(foodEntity.getFirstImageLink() != null){
            updateExpressionBuilder.append(", firstImageLink = :firstImageLink");
            updateExpressionValues.put(":firstImageLink", AttributeValue.builder().s(foodEntity.getFirstImageLink()).build());
        }

        if(foodEntity.getSecondImageLink() != null){
            updateExpressionBuilder.append(", secondImageLink = :secondImageLink");
            updateExpressionValues.put(":secondImageLink", AttributeValue.builder().s(foodEntity.getSecondImageLink()).build());
        }

        if(foodEntity.getThirdImageLink() != null){
            updateExpressionBuilder.append(", thirdImageLink = :thirdImageLink");
            updateExpressionValues.put(":thirdImageLink", AttributeValue.builder().s(foodEntity.getThirdImageLink()).build());
        }

        final String updateExpression = updateExpressionBuilder.substring(2);

        final UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .updateExpression("SET " + updateExpression)
                .conditionExpression("attribute_exists(productId)")
                .expressionAttributeValues(updateExpressionValues)
                .build();

        try {
            dynamoDbClient.updateItem(updateItemRequest);
            return "Food detail with product id:"+productId+" has updated successfully";
        }catch (DynamoDbException e){
            return "There is an error in updating request "+e.getMessage();
        }
    }

    /**
     * Retrieving food items based on the type
     */
    public static List<FoodEntity> getFoodDetailsOfType(final DynamoDbClient dynamoDbClient, final String foodType){
        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("FoodTable")
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<FoodEntity> foodEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                String itemFoodType = item.get("foodType").s();
                if (foodType.equalsIgnoreCase(itemFoodType)) {
                    FoodEntity foodEntity = new FoodEntity(
                            item.get("productId").s(),
                            item.get("productName").s(),
                            item.get("thumbnailImage").s(),
                            Integer.parseInt(item.get("productPrice").n()),
                            itemFoodType
                    );

                    if (item.containsKey("firstImageLink")) {
                        foodEntity.setFirstImageLink(item.get("firstImageLink").s());
                    }

                    if (item.containsKey("secondImageLink")) {
                        foodEntity.setSecondImageLink(item.get("secondImageLink").s());
                    }

                    if (item.containsKey("thirdImageLink")) {
                        foodEntity.setThirdImageLink(item.get("thirdImageLink").s());
                    }
                    foodEntityList.add(foodEntity);
                }
            }
            return foodEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving food details: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * getting single food detail using param.
     */
    public static FoodEntity getDetailOfSingleFood(DynamoDbClient dynamoDbClient, String productId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .build();
        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            if(getItemResponse.hasItem()){
                Map<String, AttributeValue> item = getItemResponse.item();

                String firstImageLink = item.containsKey("firstImageLink") ? item.get("firstImageLink").s() : null;
                String secondImageLink = item.containsKey("secondImageLink") ? item.get("secondImageLink").s() : null;
                String thirdImageLink = item.containsKey("thirdImageLink") ? item.get("thirdImageLink").s() : null;

                return new FoodEntity(
                        item.get("productId").s(),
                        item.get("productName").s(),
                        item.get("thumbnailImage").s(),
                        Integer.parseInt(item.get("productPrice").n()),
                        item.get("foodType").s(),
                        firstImageLink,
                        secondImageLink,
                        thirdImageLink
                );
            }
        } catch (DynamoDbException e) {
            System.out.println("there is an error while fetching food details: " + e.getMessage());
            return null;
        }
        return null;
    }


    /**
     *Deleting a particular food item
     */
    public static String deleteFoodItem(DynamoDbClient dynamoDbClient, String productId) {
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .build();
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .build();

        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            Map<String, AttributeValue> item = getItemResponse.item();
            if(item.get("productName")==null){
                return "Food with the product Id:"+productId+" not available";
            }
            else {
                dynamoDbClient.deleteItem(deleteItemRequest);
                return "Deleted successfully";
            }
        }catch (DynamoDbException e){
            return "There is a problem with deleting the food detail";
        }
    }

}
