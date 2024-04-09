package com.example.canteen.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity {
    /**
     * This is the Email id of the user and this is the primary key of the user table.
     */
    private String userEmailId;

    /**
     * This is the name of the user.
     */
    private String userName;

    /**
     * This is the phone number of the user.
     */
    private Long userPhoneNumber;

    /**
     * This is the date of birth of the user.
     */
    private String dateOfBirth;

    /**
     * This is the password of the user.
     */
    private String userPassword;

    /**
     * This is the constructor of the UserEntity class without userPassword.
     */
    public UserEntity(String userEmailId, String userName, long userPhoneNumber, String dateOfBirth) {
        this.userEmailId = userEmailId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
}
