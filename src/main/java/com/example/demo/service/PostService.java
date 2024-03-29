package com.example.demo.service;

import com.example.demo.model.PostModel;
import com.example.demo.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void addPost(PostModel postModel) {
        postRepository.save(postModel);
    }

    public boolean checkPostExists(String url) {
        return postRepository.findByUrl(url).isPresent();
    }

    public Optional<PostModel> findByPostId(String postId){
        return postRepository.findByPostId(postId);
    }

    public Optional<PostModel> findTopNullPost(){
        return postRepository.findFirstByPostOrderByPublishTimeDesc("null");
    }

    public Optional<PostModel> findTopNullTweet(){
        return postRepository.findTopByTweet("null");
    }

    public void updateTweet(PostModel postsModel) {
        postRepository.save(postsModel);
    }

    public void updatePost(PostModel postModel) {
        postRepository.save(postModel);
    }
}