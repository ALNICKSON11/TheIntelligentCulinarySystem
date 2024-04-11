package com.example.canteen.service;

import com.example.canteen.model.OrderEntity;
import com.example.canteen.model.OrderProductEntity;
import com.example.canteen.utils.OrderIdCounter;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl {

    /**
     * This method is used to add new order detail to the database.
     *
     * @return String indicating the status of the operation.
     */
    public static String addNewOrderDetail(final DynamoDbClient dynamoDbClient, final OrderEntity orderEntity) {

        int totalPrice = 0;
        for (OrderProductEntity orderProductEntity : orderEntity.getOrderProductEntityList()) {
            totalPrice += orderProductEntity.getProductCount() * orderProductEntity.getProductPrice();
        }
        orderEntity.setTotalPrice(totalPrice);

        OrderIdCounter orderIdCounter = new OrderIdCounter();
        int nextOrderId = orderIdCounter.getNextOrderId(dynamoDbClient);
        orderEntity.setOrderId(nextOrderId);

        final Map<String, AttributeValue> item = new HashMap<>();
        item.put("orderId", AttributeValue.builder().n(String.valueOf(orderEntity.getOrderId())).build());
        item.put("userEmailId", AttributeValue.builder().s(orderEntity.getUserEmailId()).build());
        item.put("paymentId", AttributeValue.builder().s(orderEntity.getPaymentId()).build());
        item.put("totalPrice", AttributeValue.builder().n(String.valueOf(orderEntity.getTotalPrice())).build());

        final List<AttributeValue> orderProductEntities = new ArrayList<>();
        for (OrderProductEntity orderProductEntity : orderEntity.getOrderProductEntityList()) {
            Map<String, AttributeValue> productItem = new HashMap<>();
            productItem.put("productId", AttributeValue.builder().s(orderProductEntity.getProductId()).build());
            productItem.put("productName", AttributeValue.builder().s(orderProductEntity.getProductName()).build());
            productItem.put("productPrice", AttributeValue.builder().n(String.valueOf(orderProductEntity.getProductPrice())).build());
            productItem.put("productCount", AttributeValue.builder().n(String.valueOf(orderProductEntity.getProductCount())).build());
            orderProductEntities.add(AttributeValue.builder().m(productItem).build());
        }
        item.put("orderProductEntityList", AttributeValue.builder().l(orderProductEntities).build());

        final PutItemRequest request = PutItemRequest.builder()
                .tableName("OrderTable")
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
        return "Order ID " + orderEntity.getOrderId() + " added successfully";
    }

    /**
     * This method is used to get all order details from the database.
     *
     * @return List of OrderEntity objects containing the order details.
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
                final List<OrderProductEntity> orderProductEntityList = new ArrayList<>();
                for (AttributeValue productAttributeValue : item.get("orderProductEntityList").l()) {
                    final Map<String, AttributeValue> productItem = productAttributeValue.m();
                    final OrderProductEntity orderProductEntity = new OrderProductEntity(
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

            Collections.sort(orderEntityList, Comparator.comparing(OrderEntity::getOrderId, Comparator.reverseOrder()));
            return orderEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving order details: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * This method is used to get order details for a particular user.
     *
     * @return List of OrderEntity objects containing the order details for the user.
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
                final OrderEntity orderEntity = new OrderEntity(
                        Integer.parseInt(item.get("orderId").n()),
                        item.get("userEmailId").s(),
                        Integer.parseInt(item.get("totalPrice").n()),
                        item.get("paymentId").s()
                );
                final List<OrderProductEntity> orderProductEntityList = new ArrayList<>();
                for (AttributeValue productAttributeValue : item.get("orderProductEntityList").l()) {
                    final Map<String, AttributeValue> productItem = productAttributeValue.m();
                    final OrderProductEntity orderProductEntity = new OrderProductEntity(
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
            Collections.sort(orderEntityList, Comparator.comparing(OrderEntity::getOrderId, Comparator.reverseOrder()));
            return orderEntityList;
        } catch (Exception e) {
            System.out.println("Error in retrieving order details: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
