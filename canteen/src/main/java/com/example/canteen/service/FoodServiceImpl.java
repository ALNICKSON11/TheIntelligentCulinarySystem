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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodServiceImpl {
    /**
     * This method is used to add new food item
     */
    public static String addNewFood(final DynamoDbClient dynamoDbClient, final FoodEntity foodEntity) {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put("productId", AttributeValue.builder().s(foodEntity.getProductId()).build());

        final GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("FoodTable")
                .key(key)
                .build();

        try {
            final GetItemResponse getItemResponse = dynamoDbClient.getItem(getRequest);
            final Map<String, AttributeValue> existingItem = getItemResponse.item();

            if (existingItem != null && !existingItem.isEmpty()) {
                return "Product ID '" + foodEntity.getProductId() + "' already used for " + foodEntity.getProductName();
            } else {
                final Map<String, AttributeValue> item = new HashMap<>();
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

                final PutItemRequest request = PutItemRequest.builder()
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
     * This method is used to retrieve all food items from the food table.
     */
    public static List<FoodEntity> getAllFoodItems(final DynamoDbClient dynamoDbClient) {
        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("FoodTable")
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<FoodEntity> foodEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                final FoodEntity foodEntity = new FoodEntity(
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
            Collections.sort(foodEntityList, Comparator.comparingInt(foodEntity -> Integer.parseInt(foodEntity.getProductId())));

            return foodEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving food details: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * This method is used to edit the details of a particular food item.
     *
     * @return String to mention the success or failure of the operation.
     */
    public static String editFoodDetail(final DynamoDbClient dynamoDbClient, final String productId, final FoodEntity foodEntity) {
        final Map<String, AttributeValue> updateExpressionValues = new HashMap<>();
        final StringBuilder updateExpressionBuilder = new StringBuilder();

        if (foodEntity.getProductName() != null) {
            updateExpressionBuilder.append(", productName = :productName");
            updateExpressionValues.put(":productName", AttributeValue.builder().s(foodEntity.getProductName()).build());
        }

        if (foodEntity.getThumbnailImage() != null) {
            updateExpressionBuilder.append(", thumbnailImage = :thumbnailImage");
            updateExpressionValues.put(":thumbnailImage", AttributeValue.builder().s(foodEntity.getThumbnailImage()).build());
        }

        if (foodEntity.getProductPrice() != 0) {
            updateExpressionBuilder.append(", productPrice = :productPrice");
            updateExpressionValues.put(":productPrice", AttributeValue.builder().n(String.valueOf(foodEntity.getProductPrice())).build());
        }

        if (foodEntity.getFirstImageLink() != null) {
            updateExpressionBuilder.append(", firstImageLink = :firstImageLink");
            updateExpressionValues.put(":firstImageLink", AttributeValue.builder().s(foodEntity.getFirstImageLink()).build());
        }

        if (foodEntity.getSecondImageLink() != null) {
            updateExpressionBuilder.append(", secondImageLink = :secondImageLink");
            updateExpressionValues.put(":secondImageLink", AttributeValue.builder().s(foodEntity.getSecondImageLink()).build());
        }

        if (foodEntity.getThirdImageLink() != null) {
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
            return "Food detail with product id:" + productId + " has updated successfully";
        } catch (DynamoDbException e) {
            return "There is an error in updating request " + e.getMessage();
        }
    }

    /**
     * This method is used to retrieve the food items of a particular type.
     *
     * @return List of food items of a particular type.
     */
    public static List<FoodEntity> getFoodDetailsOfType(final DynamoDbClient dynamoDbClient, final String foodType) {
        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("FoodTable")
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<FoodEntity> foodEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                final String itemFoodType = item.get("foodType").s();
                if (foodType.equalsIgnoreCase(itemFoodType)) {
                    final FoodEntity foodEntity = new FoodEntity(
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
            Collections.sort(foodEntityList, Comparator.comparingInt(foodEntity -> Integer.parseInt(foodEntity.getProductId())));
            return foodEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving food details: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * This method is used to delete a particular food item.
     *
     * @return String to mention the success or failure of the operation.
     */
    public static FoodEntity getDetailOfSingleFood(final DynamoDbClient dynamoDbClient, final String productId) {
        final GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .build();
        try {
            final GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            if (getItemResponse.hasItem()) {
                final Map<String, AttributeValue> item = getItemResponse.item();

                final String firstImageLink = item.containsKey("firstImageLink") ? item.get("firstImageLink").s() : null;
                final String secondImageLink = item.containsKey("secondImageLink") ? item.get("secondImageLink").s() : null;
                final String thirdImageLink = item.containsKey("thirdImageLink") ? item.get("thirdImageLink").s() : null;

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
     * This method is used to retrieve the details of a particular food item.
     *
     * @return FoodEntity object containing the details of the food item.
     */
    public static String deleteFoodItem(final DynamoDbClient dynamoDbClient, final String productId) {
        final DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .build();
        final GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("FoodTable")
                .key(Collections.singletonMap("productId", AttributeValue.builder().s(productId).build()))
                .build();

        try {
            final GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            final Map<String, AttributeValue> item = getItemResponse.item();
            if (item.get("productName") == null) {
                return "Food with the product Id:" + productId + " not available";
            } else {
                dynamoDbClient.deleteItem(deleteItemRequest);
                return "Deleted successfully";
            }
        } catch (DynamoDbException e) {
            return "There is a problem with deleting the food detail";
        }
    }

}
