package com.example.canteen.service;

import com.example.canteen.model.UserEntity;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl {
    /**
     * getting all user details.
     */
    public static List<UserEntity> getAllUserDetails(final DynamoDbClient dynamoDbClient){
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("smartCanteenTable")
                .build();

        try {
            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            List<UserEntity> userEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()){
                UserEntity userEntity = new UserEntity(
                        item.get("userEmailId").s(),
                        item.get("userName").s(),
                        Long.parseLong(item.get("userPhoneNumber").n()),
                        item.get("dateOfBirth").s()
                );
                userEntityList.add(userEntity);
            }
            return userEntityList;
        }catch (Exception e){
            System.out.println("Error in retrieving user details"+e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * adding new user
     */
    public static String addNewUser(final DynamoDbClient dynamoDbClient, final UserEntity userEntity){
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("userEmailId", AttributeValue.builder().s(userEntity.getUserEmailId()).build());

        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("smartCanteenTable")
                .key(key)
                .build();

        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getRequest);
            Map<String, AttributeValue> existingItem = getItemResponse.item();

            if (existingItem != null && !existingItem.isEmpty()) {
                return "This EmailID: " +userEntity.getUserEmailId()+" already exist";
            } else {
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("userEmailId", AttributeValue.builder().s(userEntity.getUserEmailId()).build());
                item.put("userName", AttributeValue.builder().s(userEntity.getUserName()).build());
                item.put("userPhoneNumber", AttributeValue.builder().n(String.valueOf(userEntity.getUserPhoneNumber())).build());
                item.put("dateOfBirth", AttributeValue.builder().s(userEntity.getDateOfBirth()).build());
                item.put("userPassword", AttributeValue.builder().s(userEntity.getUserPassword()).build());

                PutItemRequest request = PutItemRequest.builder()
                        .tableName("smartCanteenTable")
                        .item(item)
                        .build();

                dynamoDbClient.putItem(request);
                return "The user " +userEntity.getUserName() + " added successfully";
            }
        } catch (DynamoDbException e) {
            return "Error in adding user: " + e.getMessage();
        }
    }

    /**
     * getting single student details using param.
     */
    public static UserEntity getSingleUserDetail(DynamoDbClient dynamoDbClient, String userEmailId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("smartCanteenTable")
                .key(Collections.singletonMap("userEmailId", AttributeValue.builder().s(userEmailId).build()))
                .build();
        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
            if(getItemResponse.hasItem()){
                Map<String, AttributeValue> item = getItemResponse.item();
                return new UserEntity(
                        item.get("userEmailId").s(),
                        item.get("userName").s(),
                        Long.parseLong(item.get("userPhoneNumber").n()),
                        item.get("dateOfBirth").s()
                );
            }
        }catch (DynamoDbException e) {
            System.out.println("there is a error while fetching student details" + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * User Login
     */
    public static String userLogin(DynamoDbClient dynamoDbClient, String userEmailId, String userPassword) {
        Map<String, AttributeValue> keyMap = new HashMap<>();
        keyMap.put("userEmailId", AttributeValue.builder().s(userEmailId).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("smartCanteenTable")
                .key(keyMap)
                .build();

        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

            if (getItemResponse.hasItem()) {
                AttributeValue storedPassword = getItemResponse.item().get("userPassword");

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
