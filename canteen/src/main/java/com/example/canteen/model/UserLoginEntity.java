package com.example.canteen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginEntity {
    private String userEmailId;
    private String userPassword;
}
