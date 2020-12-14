package com.example.demo.service;

import com.example.demo.model.PostsModel;
import com.example.demo.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostsService {
    private PostsRepository postsRepository;

    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public void addPost(PostsModel postsModel) {
        postsRepository.save(postsModel);
    }

    public boolean checkPostExists(String url) {
        if(postsRepository.findByUrl(url).isPresent()){
            return true;
        } else {
            return false;
        }
    }

    public Optional<PostsModel> findByPostId(String postId){
        return postsRepository.findTopByPostId(postId);
    }

    public void updateNewsPostSocialMediaStatus(PostsModel postsModel) {
        postsRepository.save(postsModel);
    }
}
