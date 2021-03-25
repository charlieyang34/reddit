package com.example.reddit.model;

import lombok.*;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}