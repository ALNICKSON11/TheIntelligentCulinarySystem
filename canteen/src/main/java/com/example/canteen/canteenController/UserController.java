package com.example.canteen.canteenController;

import com.example.canteen.canteenInterface.ConnectionInterface;
import com.example.canteen.canteenInterface.UserInterface;
import com.example.canteen.model.UserEntity;
import com.example.canteen.model.UserLoginEntity;
import com.example.canteen.module.CanteenModule;
import com.example.canteen.service.UserServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

@RestController
public class UserController implements UserInterface {
    private final Injector injector = Guice.createInjector(new CanteenModule());
    private final ConnectionInterface request = injector.getInstance(ConnectionInterface.class);
    @Inject
    private final DynamoDbClient dynamoDbClient = request.getDdbClient();

    @Override
    public List<UserEntity> getAllUsersDetail(){
        return UserServiceImpl.getAllUserDetails(dynamoDbClient);
    }

    @Override
    public String addNewUser(@RequestBody final UserEntity userEntity){
        return UserServiceImpl.addNewUser(dynamoDbClient,userEntity);
    }
    @Override
    public UserEntity getSingleUserDetail(@PathVariable final String userEmailId){
        return UserServiceImpl.getSingleUserDetail(dynamoDbClient, userEmailId);
    }
    public String userLogin(@RequestBody final UserLoginEntity userLoginEntity){
        String userEmailId = userLoginEntity.getUserEmailId();
        String userPassword = userLoginEntity.getUserPassword();
        return UserServiceImpl.userLogin(dynamoDbClient, userEmailId, userPassword);
    }
}
