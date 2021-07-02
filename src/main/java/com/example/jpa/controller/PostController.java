package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Post;
import com.example.jpa.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PostController {

    @Autowired
    private PostRepository repository;

    @GetMapping("/get-all")
    public Page<Post> getAllPosts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping("/create")
    public Post createPost(@Valid @RequestBody Post post) {
        return repository.save(post);
    }

    @PutMapping("/update/{postId}")
    public Post updatePost(@PathVariable("postId") Long postId, @Valid @RequestBody Post postRequest) {
        return repository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            return repository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @DeleteMapping("/delete{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId){
        return repository.findById(postId).map(post -> {
            repository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
}
