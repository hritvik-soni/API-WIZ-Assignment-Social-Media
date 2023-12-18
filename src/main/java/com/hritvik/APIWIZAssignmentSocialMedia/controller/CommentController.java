package com.hritvik.APIWIZAssignmentSocialMedia.controller;

import com.hritvik.APIWIZAssignmentSocialMedia.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Create comment endpoint
     * @param authentication taking username from login info
     * @param postId taking postId
     * @param comment taking String comment
     * @return message of success or failure
     *
     */
    @PostMapping("/new/{postId}")
    public ResponseEntity<String> newComment (Authentication authentication, @PathVariable Long postId,@RequestParam("comment") String comment){
        String username = authentication.getName();
        return commentService.addComment(username,postId,comment);
    }

    /**
     * delete comment endpoint
     * @param authentication taking username from login info
     * @param postId taking postId
     * @param commentId taking commentId
     * @return message of success or failure
     */

    @DeleteMapping("/remove/{postId}/{commentId}")
    public ResponseEntity<String> removeComment (Authentication authentication, @PathVariable Long postId,@PathVariable Long commentId){
        String username = authentication.getName();
        return commentService.removeComment(username,postId,commentId);
    }


}
