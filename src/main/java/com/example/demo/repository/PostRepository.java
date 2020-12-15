package com.example.demo.repository;

import com.example.demo.model.PostModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PostRepository extends MongoRepository<PostModel, String> {
    Optional<PostModel> findByUrl(String url);
    Optional<PostModel> findTopByPostId(String postId);
}