package com.ApiServer.java.Repository;

import com.ApiServer.java.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByUsername(String username);
}
