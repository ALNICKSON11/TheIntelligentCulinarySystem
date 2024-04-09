package com.example.canteen.canteenController;

import com.example.canteen.canteenInterface.ConnectionInterface;
import com.example.canteen.canteenInterface.OrderInterface;
import com.example.canteen.model.OrderEntity;
import com.example.canteen.module.CanteenModule;
import com.example.canteen.service.OrderServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

@RestController
public class OrderController implements OrderInterface {

    private final Injector injector = Guice.createInjector(new CanteenModule());
    private final ConnectionInterface request = injector.getInstance(ConnectionInterface.class);
    @Inject
    private final DynamoDbClient dynamoDbClient = request.getDdbClient();

    /**
     * This method is used to add new order detail to the database.
     *
     * @return String indicating the status of the operation.
     */
    @Override
    public String addNewOrderDetail(@RequestBody final OrderEntity orderEntity) {
        return OrderServiceImpl.addNewOrderDetail(dynamoDbClient, orderEntity);
    }

    /**
     * This method is used to get all order details from the database.
     *
     * @return List of OrderEntity objects containing the order details.
     */
    @Override
    public List<OrderEntity> getAllOrderDetails() {
        return OrderServiceImpl.getAllOrderDetails(dynamoDbClient);
    }

    /**
     * This method is used to get order details for a particular user.
     *
     * @return List of OrderEntity objects containing the order details for the user.
     */
    @Override
    public List<OrderEntity> getOrderDetailsForUser(@PathVariable String userEmailId) {
        return OrderServiceImpl.getOrderDetailsForUser(dynamoDbClient, userEmailId);
    }
}
