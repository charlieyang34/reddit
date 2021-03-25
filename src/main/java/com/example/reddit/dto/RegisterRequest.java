package com.example.reddit.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class RegisterRequest {

    private String username;
    private String password;
    private String email;
}
