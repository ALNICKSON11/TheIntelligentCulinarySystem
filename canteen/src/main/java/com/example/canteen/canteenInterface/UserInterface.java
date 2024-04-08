package com.example.canteen.canteenInterface;

import com.example.canteen.model.UserEntity;
import com.example.canteen.model.UserLoginEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserInterface {
    /**
     * Get details of all users.
     */
    @GetMapping("/get")
    public List<UserEntity> getAllUsersDetail();

    /**
     * Add new user.
     */
    @PostMapping("/add")
    public String addNewUser(@RequestBody UserEntity userEntity);

    /**
     * Get details of a single user.
     */
    @GetMapping("/user/{userEmailId}")
    public UserEntity getSingleUserDetail(@PathVariable final String userEmailId);

    /**
     * User Login.
     */
    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginEntity userLoginEntity);
}
