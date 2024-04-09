package com.example.canteen.canteenInterface;

import com.example.canteen.model.UserEntity;
import com.example.canteen.model.UserLoginEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserInterface {
    /**
     * This method is used to get all the user details from database.
     *
     * @return List of UserEntity.
     */
    @GetMapping("/get")
    public List<UserEntity> getAllUsersDetail();

    /**
     * This method is used to add new user to database.
     *
     * @return String to mention the success or failure of the operation.
     */
    @PostMapping("/add")
    public String addNewUser(@RequestBody UserEntity userEntity);

    /**
     * This method is used to retrieve single user detail from database.
     *
     * @return UserEntity object containing the user details.
     */
    @GetMapping("/user/{userEmailId}")
    public UserEntity getSingleUserDetail(@PathVariable final String userEmailId);

    /**
     * This method is used to login user.
     *
     * @return String to mention the success or failure of the operation.
     */
    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginEntity userLoginEntity);
}
