package com.example.canteen.service;

import com.example.canteen.model.OrderEntity;
import com.example.canteen.model.OrderProductEntity;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl {

    public static String addNewOrderDetail(final DynamoDbClient dynamoDbClient, final OrderEntity orderEntity) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("orderId", AttributeValue.builder().n(String.valueOf(orderEntity.getOrderId())).build());

        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName("OrderTable")
                .key(key)
                .build();

        try {
            GetItemResponse getItemResponse = dynamoDbClient.getItem(getRequest);
            Map<String, AttributeValue> existingItem = getItemResponse.item();

            if (existingItem != null && !existingItem.isEmpty()) {
                return "Order ID '" + orderEntity.getOrderId() + "' already exists";
            } else {
                int totalPrice = 0;
                for (OrderProductEntity orderProductEntity : orderEntity.getOrderProductEntityList()) {
                    totalPrice += orderProductEntity.getProductCount() * orderProductEntity.getProductPrice();
                }
                orderEntity.setTotalPrice(totalPrice);

                Map<String, AttributeValue> item = new HashMap<>();
                item.put("orderId", AttributeValue.builder().n(String.valueOf(orderEntity.getOrderId())).build());
                item.put("userEmailId", AttributeValue.builder().s(orderEntity.getUserEmailId()).build());
                item.put("paymentId", AttributeValue.builder().s(orderEntity.getPaymentId()).build());
                item.put("totalPrice", AttributeValue.builder().n(String.valueOf(orderEntity.getTotalPrice())).build());

                // Construct list of order product entities
                List<AttributeValue> orderProductEntities = new ArrayList<>();
                for (OrderProductEntity orderProductEntity : orderEntity.getOrderProductEntityList()) {
                    Map<String, AttributeValue> productItem = new HashMap<>();
                    productItem.put("productId", AttributeValue.builder().s(orderProductEntity.getProductId()).build());
                    productItem.put("productName", AttributeValue.builder().s(orderProductEntity.getProductName()).build());
                    productItem.put("productPrice", AttributeValue.builder().n(String.valueOf(orderProductEntity.getProductPrice())).build());
                    productItem.put("productCount", AttributeValue.builder().n(String.valueOf(orderProductEntity.getProductCount())).build());
                    orderProductEntities.add(AttributeValue.builder().m(productItem).build());
                }
                item.put("orderProductEntityList", AttributeValue.builder().l(orderProductEntities).build());

                PutItemRequest request = PutItemRequest.builder()
                        .tableName("OrderTable")
                        .item(item)
                        .build();

                dynamoDbClient.putItem(request);
                return "Order ID " + orderEntity.getOrderId() + " added successfully";
            }
        } catch (DynamoDbException e) {
            return "Error in adding order: " + e.getMessage();
        }
    }

    /**
     * getting all order details
     */
    public static List<OrderEntity> getAllOrderDetails(final DynamoDbClient dynamoDbClient) {
        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("OrderTable")
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<OrderEntity> orderEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                OrderEntity orderEntity = new OrderEntity(
                        Integer.parseInt(item.get("orderId").n()),
                        item.get("userEmailId").s(),
                        Integer.parseInt(item.get("totalPrice").n()),
                        item.get("paymentId").s()
                );
                List<OrderProductEntity> orderProductEntityList = new ArrayList<>();
                for (AttributeValue productAttributeValue : item.get("orderProductEntityList").l()) {
                    Map<String, AttributeValue> productItem = productAttributeValue.m();
                    OrderProductEntity orderProductEntity = new OrderProductEntity(
                            productItem.get("productId").s(),
                            productItem.get("productName").s(),
                            Integer.parseInt(productItem.get("productCount").n()),
                            Integer.parseInt(productItem.get("productPrice").n())
                    );
                    orderProductEntityList.add(orderProductEntity);
                }
                orderEntity.setOrderProductEntityList(orderProductEntityList);
                orderEntityList.add(orderEntity);
            }
            return orderEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving order details: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * Getting order details for a single user
     */
    public static List<OrderEntity> getOrderDetailsForUser(final DynamoDbClient dynamoDbClient, final String userEmailId) {
        final Map<String, AttributeValue> attributeValues = Collections.singletonMap(":val", AttributeValue.builder().s(userEmailId).build());

        final ScanRequest scanRequest = ScanRequest.builder()
                .tableName("OrderTable")
                .filterExpression("userEmailId = :val")
                .expressionAttributeValues(attributeValues)
                .build();

        try {
            final ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            final List<OrderEntity> orderEntityList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                OrderEntity orderEntity = new OrderEntity(
                        Integer.parseInt(item.get("orderId").n()),
                        item.get("userEmailId").s(),
                        Integer.parseInt(item.get("totalPrice").n()),
                        item.get("paymentId").s()
                );
                List<OrderProductEntity> orderProductEntityList = new ArrayList<>();
                for (AttributeValue productAttributeValue : item.get("orderProductEntityList").l()) {
                    Map<String, AttributeValue> productItem = productAttributeValue.m();
                    OrderProductEntity orderProductEntity = new OrderProductEntity(
                            productItem.get("productId").s(),
                            productItem.get("productName").s(),
                            Integer.parseInt(productItem.get("productCount").n()),
                            Integer.parseInt(productItem.get("productPrice").n())
                    );
                    orderProductEntityList.add(orderProductEntity);
                }
                orderEntity.setOrderProductEntityList(orderProductEntityList);
                orderEntityList.add(orderEntity);
            }
            return orderEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving order details: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
