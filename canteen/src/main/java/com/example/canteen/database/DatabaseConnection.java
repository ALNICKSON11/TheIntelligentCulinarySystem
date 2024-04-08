package com.example.canteen.database;

import com.example.canteen.canteenInterface.ConnectionInterface;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static com.example.canteen.database.DatabaseConstants.ACCESS_KEY_ID;
import static com.example.canteen.database.DatabaseConstants.REGION_ID;
import static com.example.canteen.database.DatabaseConstants.SECRET_ACCESS_KEY;

public class DatabaseConnection implements ConnectionInterface {
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(REGION_ID)
            .credentialsProvider(() -> AwsBasicCredentials.create(ACCESS_KEY_ID, SECRET_ACCESS_KEY))
            .build();

    public DynamoDbClient getDdbClient() {
        return dynamoDbClient;
    }
}
