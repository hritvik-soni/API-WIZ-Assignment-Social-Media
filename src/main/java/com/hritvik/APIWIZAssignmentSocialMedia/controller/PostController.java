package com.hritvik.APIWIZAssignmentSocialMedia.controller;


import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Privacy;
import com.hritvik.APIWIZAssignmentSocialMedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Creating new post for the user
     * @param authentication using for extracting long info(username)
     * @param file taking file as input(contains - text,image,video)
     * @param privacy taking privacy as input for post privacy
     * @return returning success or failure message
     * @throws IOException throwing input error
     */
    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> createPost(Authentication authentication,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("privacy") Privacy privacy) throws IOException {
        String username = authentication.getName();

        return  postService.createPost( file,username,privacy);
    }

    /**
     * Searching for Posts by userId
     * @param authentication using for extracting long info(username)
     * @param userId taking usedId as input for searching
     * @return returning success or failure message (List of Post, fail message)
     */
    @GetMapping("/of/{userId}")
//    @PreAuthorize("hasAuthority('ROLE_USER,ROLE_ADMIN')")
    public ResponseEntity<List<?>> getPostsByUserId(Authentication authentication, @PathVariable Long userId) {

        String username =authentication.getName();

        return  postService.getPostsByUserId(userId,username);
    }

    /**
     * removing post
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning success or failure message
     */

    @DeleteMapping("/{postId}")
//    @PreAuthorize("hasAuthority('ROLE_USER,ROLE_ADMIN')")
    public ResponseEntity<String> deletePost(Authentication authentication,@PathVariable Long postId) {
        String username = authentication.getName();
        return  postService.deletePost(postId,username);
    }

    /**
     * creating post reshare
     * @param authentication using for extracting long info(username)
     * @param postId taking postId as input
     * @return returning success or failure message
     */

    @PostMapping("/reShare/{postId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> reSharePost(Authentication authentication, @PathVariable Long postId) {
       String username = authentication.getName();
        return postService.reSharePost(postId,username);
    }

    /**
     * searching / returning specific post
     * @param postId taking postId as input
     * @return returning success or failure message
     */
    @GetMapping("/by/{postId}")
    public ResponseEntity<byte[]> postById(@PathVariable Long postId) {

        return postService.postById(postId);
    }




}
