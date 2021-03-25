package com.example.reddit.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data

public class LoginRequest {

    //more complex logic can be implemented using username and password validator （工具类）

    @NotNull
    @Min(5)
    private String username;
    @NotNull
    @Min(5)
    private String password;
}
