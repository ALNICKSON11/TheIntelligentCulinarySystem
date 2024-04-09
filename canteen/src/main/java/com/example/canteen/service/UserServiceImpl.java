package com.example.canteen.service;

import com.example.canteen.model.UserEntity;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl {
    /**
     * This method is used to get all the user details from database.
     *
     * @return List of UserEntity.
     */
    public static List<UserEntity> getAllUserDetails(final DynamoDbClient dynamoDbClient) {
        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("smartCanteenTable")
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<UserEntity> userEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                final UserEntity userEntity = new UserEntity(
                        item.get("userEmailId").s(),
                        item.get("userName").s(),
                        Long.parseLong(item.get("userPhoneNumber").n()),
                        item.get("dateOfBirth").s()
                );
                userEntityList.add(userEntity);
            }
            return userEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving user details" + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * This method is used to add new user to database.
     *
     * @return String to mention the success or failure of the operation.
     */
    public static String addNewUser(final DynamoDbClient dynamoDbClient, final UserEntity userEntity) {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put("userEmailId", AttributeValue.builder().s(userEntity.getUserEmailId()).build());

        final GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("smartCanteenTable")
                .key(key)
                .build();

        try {
            final GetItemResponse getItemResponse = dynamoDbClient.getItem(getRequest);
            final Map<String, AttributeValue> existingItem = getItemResponse.item();

            if (existingItem != null && !existingItem.isEmpty()) {
                return "This EmailID: " + userEntity.getUserEmailId() + " already exist";
            } else {
                final Map<String, AttributeValue> item = new HashMap<>();
                item.put("userEmailId", AttributeValue.builder().s(userEntity.getUserEmailId()).build());
                item.put("userName", AttributeValue.builder().s(userEntity.getUserName()).build());
                item.put("userPhoneNumber", AttributeValue.builder().n(String.valueOf(userEntity.getUserPhoneNumber())).build());
                item.put("dateOfBirth", AttributeValue.builder().s(userEntity.getDateOfBirth()).build());
                item.put("userPassword", AttributeValue.builder().s(userEntity.getUserPassword()).build());

                final PutItemRequest request = PutItemRequest.builder()
                        .tableName("smartCanteenTable")
                        .item(item)
                        .build();

                dynamoDbClient.putItem(request);
                return "The user " + userEntity.getUserName() + " added successfully";
            }
        } catch (DynamoDbException e) {
            return "Error in adding user: " + e.getMessage();
        }
    }

    /**
     * This method is used to retrieve single user detail from database.
     *
     * @return UserEntity object containing the user details.
     */
    public static UserEntity getSingleUserDetail(final DynamoDbClient dynamoDbClient, final String userEmailId) {
        final GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("smartCanteenTable")
                .key(Collections.singletonMap("userEmailId", AttributeValue.builder().s(userEmailId).build()))
                .build();
        try {
            final GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            if (getItemResponse.hasItem()) {
                Map<String, AttributeValue> item = getItemResponse.item();
                return new UserEntity(
                        item.get("userEmailId").s(),
                        item.get("userName").s(),
                        Long.parseLong(item.get("userPhoneNumber").n()),
                        item.get("dateOfBirth").s()
                );
            }
        } catch (DynamoDbException e) {
            System.out.println("there is a error while fetching student details" + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * This method is used to login user.
     *
     * @return String to mention the success or failure of the operation.
     */
    public static String userLogin(final DynamoDbClient dynamoDbClient, final String userEmailId, final String userPassword) {
        final Map<String, AttributeValue> keyMap = new HashMap<>();
        keyMap.put("userEmailId", AttributeValue.builder().s(userEmailId).build());

        final GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("smartCanteenTable")
                .key(keyMap)
                .build();

        try {
            final GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

            if (getItemResponse.hasItem()) {
                final AttributeValue storedPassword = getItemResponse.item().get("userPassword");

                if (storedPassword != null && storedPassword.s().equals(userPassword)) {
                    return "User login successful";
                } else {
                    return "Incorrect User password";
                }
            } else {
                return "User not found";
            }
        } catch (DynamoDbException e) {
            return "Error in User login: " + e.getMessage();
        }
    }
}
