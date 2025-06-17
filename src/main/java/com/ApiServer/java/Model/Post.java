package com.ApiServer.java.Model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content;
    private String username;
    private int likeCount = 0;
}
