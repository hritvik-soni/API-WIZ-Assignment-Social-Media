package com.hritvik.APIWIZAssignmentSocialMedia.controller;

import com.hritvik.APIWIZAssignmentSocialMedia.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * creating like for a post
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning message for success of failure
     */
    @PostMapping("/new/{postId}")
    public ResponseEntity<String> newLike (Authentication authentication, @PathVariable Long postId){
        String username = authentication.getName();
        return likeService.addLike(username,postId);

    }

    /**
     * removing like from a post
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning message for success of failure
     */
    @DeleteMapping("/remove/{postId}")
    public ResponseEntity<String> removeLike (Authentication authentication, @PathVariable Long postId){
        String username = authentication.getName();
        return likeService.removeLike(username,postId);
    }



}
