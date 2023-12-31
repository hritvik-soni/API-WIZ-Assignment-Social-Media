package com.hritvik.APIWIZAssignmentSocialMedia.controller;

import com.hritvik.APIWIZAssignmentSocialMedia.service.CommentService;
import com.hritvik.APIWIZAssignmentSocialMedia.service.LikeService;
import com.hritvik.APIWIZAssignmentSocialMedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class UserAnalyticsController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    /**
     * Searching for like counts on the post
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning success or failure message
     */

    @GetMapping("/likeCount/{postId}")
    public ResponseEntity<String> countLike (Authentication authentication, @PathVariable Long postId){
        String username = authentication.getName();
        return likeService.countLike(username,postId);
    }
    /**
     * Searching for reShare counts on the post
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning success or failure message
     */


    @GetMapping("/reShareCount/{postId}")
    public ResponseEntity<String> reShareCount(Authentication authentication, @PathVariable Long postId) {
        String username = authentication.getName();
        return postService.reShareCount(postId,username);
    }
    /**
     * Searching for comments counts on the post
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning success or failure message
     */

    @GetMapping("/commentCount/{postId}")
    public ResponseEntity<String> countComment (Authentication authentication, @PathVariable Long postId){
        String username = authentication.getName();
        return commentService.countComment(username,postId);
    }

}
