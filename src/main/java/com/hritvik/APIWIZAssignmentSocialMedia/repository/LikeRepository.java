package com.hritvik.APIWIZAssignmentSocialMedia.repository;

import com.hritvik.APIWIZAssignmentSocialMedia.model.Like;
import com.hritvik.APIWIZAssignmentSocialMedia.model.Post;
import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {

    List<Like> findByInstaPostAndLiker(Post instaPost, User liker);

    List<Like> findByInstaPost(Post validPost);
}
