package com.example.reddit.model;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Comment{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)
    private User user;


}