package com.example.canteen.canteenInterface;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface ConnectionInterface {
    public DynamoDbClient getDdbClient();
}
