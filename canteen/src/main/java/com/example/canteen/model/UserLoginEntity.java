package com.example.canteen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This is the model class for user login
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginEntity {
    /**
     * This is the email id of the user who is trying to login
     */
    private String userEmailId;

    /**
     * This is the password of the user who is trying to login
     */
    private String userPassword;
}
