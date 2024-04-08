package com.example.canteen.database;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.util.Collections;
import java.util.List;

public class OrderTable {
    public static void createOrderTable(DynamoDbClient ddbClient) {
        List<AttributeDefinition> attributeDefinitions = List.of(
                AttributeDefinition.builder()
                        .attributeName("orderId")
                        .attributeType(ScalarAttributeType.N)
                        .build());

        List<KeySchemaElement> keySchema = Collections.singletonList(
                KeySchemaElement.builder()
                        .attributeName("orderId")
                        .keyType(KeyType.HASH)
                        .build());

        CreateTableRequest request = CreateTableRequest.builder()
                .tableName("OrderTable")
                .attributeDefinitions(attributeDefinitions)
                .keySchema(keySchema)
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(5L)
                        .build())
                .build();

        try {
            ddbClient.createTable(request);
            System.out.println("Order table created successfully!");
        } catch (DynamoDbException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
}
