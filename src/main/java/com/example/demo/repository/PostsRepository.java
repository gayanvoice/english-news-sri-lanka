package com.example.demo.repository;

import com.example.demo.model.PostsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PostsRepository extends MongoRepository<PostsModel, String> {
    Optional<PostsModel> findByUrl(String url);
    Optional<PostsModel> findTopByPostId(String postId);
}