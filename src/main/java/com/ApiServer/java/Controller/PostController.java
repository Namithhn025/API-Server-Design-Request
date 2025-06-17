package com.ApiServer.java.Controller;

import com.ApiServer.java.Model.Post;
import com.ApiServer.java.Repository.PostRepository;
import com.ApiServer.java.Security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Extracts username from JWT token in request header
    private String extractUsernameFromRequest(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            return jwtUtil.extractUsername(token);
        } catch (Exception e) {
            logger.error("Failed to extract username from token", e);
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid or missing token");
        }
    }

    // Create a new post
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post, HttpServletRequest request) {
        try {
            String username = extractUsernameFromRequest(request);
            post.setUsername(username);
            Post savedPost = postRepository.save(post);

            logger.info("New post created by user: {}, Post ID: {}", username, savedPost.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Post created successfully");
            response.put("postId", savedPost.getId());

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error while creating post: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    // Like a post
    @PostMapping("/like/{id}")
    public ResponseEntity<?> likePost(@PathVariable String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Like failed: Post not found - {}", id);
                    return new ResponseStatusException(NOT_FOUND, "Post not found");
                });

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        logger.info("Post liked successfully. ID = {}", id);

        return ResponseEntity.ok("Post liked");
    }

    // Delete a post
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id, HttpServletRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Delete failed: Post not found - {}", id);
                    return new ResponseStatusException(NOT_FOUND, "Post not found");
                });

        String username = extractUsernameFromRequest(request);
        if (!post.getUsername().equals(username)) {
            logger.warn("Unauthorized delete attempt by user: {} on post ID: {}", username, id);
            throw new ResponseStatusException(FORBIDDEN, "You are not allowed to delete this post");
        }

        postRepository.delete(post);
        logger.info("Post deleted successfully. ID = {}, by user: {}", id, username);

        return ResponseEntity.ok("Post deleted");
    }

    // List all posts
    @GetMapping("/all")
    public List<Post> listPosts() {
        logger.info("Fetching all posts");
        return postRepository.findAll();
    }
}
