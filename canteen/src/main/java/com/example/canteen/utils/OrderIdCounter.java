package com.example.canteen.utils;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.util.Collections;

public class OrderIdCounter {
    private static final String TABLE_NAME = "OrderIdCounter";
    private static final String COUNTER_ATTRIBUTE = "orderIdCounter";

    public Integer getNextOrderId(final DynamoDbClient dynamoDbClient) {
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Collections.singletonMap("CounterName", AttributeValue.builder().s(COUNTER_ATTRIBUTE).build()))
                .updateExpression("SET CounterValue = CounterValue + :incr")
                .expressionAttributeValues(Collections.singletonMap(":incr", AttributeValue.builder().n("1").build()))
                .returnValues(ReturnValue.UPDATED_NEW)
                .build();

        UpdateItemResponse response = dynamoDbClient.updateItem(updateRequest);
        AttributeValue updatedValue = response.attributes().get("CounterValue");
        return Integer.parseInt(updatedValue.n());
    }
}
