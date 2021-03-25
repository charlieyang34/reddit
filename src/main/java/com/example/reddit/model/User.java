package com.example.reddit.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @Email
    @NotEmpty (message = "email required to register")
    private String email;
    private Instant created;
    private Boolean enabled;
}
