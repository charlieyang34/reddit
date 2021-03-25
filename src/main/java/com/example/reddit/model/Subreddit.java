package com.example.reddit.model;

import lombok.*;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder

public class Subreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> post;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
