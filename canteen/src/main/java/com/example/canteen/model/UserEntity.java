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
    private String userEmailId;
    private String userName;
    private Long userPhoneNumber;
    private String dateOfBirth;
    private String userPassword;

    public UserEntity(String userEmailId, String userName, long userPhoneNumber, String dateOfBirth) {
        this.userEmailId = userEmailId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
}
