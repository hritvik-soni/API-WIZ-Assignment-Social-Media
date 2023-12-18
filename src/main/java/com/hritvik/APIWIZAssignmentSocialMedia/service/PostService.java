package com.hritvik.APIWIZAssignmentSocialMedia.service;


import com.hritvik.APIWIZAssignmentSocialMedia.model.Post;
import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.PostOutput;
import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Privacy;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.FollowRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.PostRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FollowService followService;

    @Autowired
    UserService userService;


    public ResponseEntity<String> createPost(MultipartFile file, String username, Privacy privacy) throws IOException {
        if (userService.isAccountEnable(username)) {
            //  logic for creating a post
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());


            Post post = Post.builder()
                    .postName(fileName)
                    .postData(file.getBytes())
                    .postType(file.getContentType())
                    .privacy(privacy)
                    .shareCount(0L)
                    .user(userRepository.findByUserName(username).get())
                    .build();


            postRepository.save(post);
            String url = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/post/by/")
                    .path(String.valueOf(post.getId()))
                    .toUriString();
            post.setPostUrl(url);
            postRepository.save(post);

            return new ResponseEntity<>("Post Created successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Account is Disabled ", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


    public ResponseEntity<List<?>> getPostsByUserId(Long userId, String username) {
        // logic for getting posts by user ID
        if (userService.isAccountEnable(username)) {
            ArrayList<PostOutput> newList = new ArrayList<PostOutput>();
            Optional<User> user = userRepository.findByUserName(username);


            String userRole = user.get().getRole();

            if (userRole.equals("ROLE_ADMIN")) {
                return getListResponseEntity(userId, newList);
            }
            if (Objects.equals(userId, user.get().getId())) {
                return getListResponseEntity(userId, newList);
            }

            if (followService.isFollowing(username, userId)) {
                return getListResponseEntity(userId, newList);
            }

            for (Post post : postRepository.findAll()) {
                if (post.getPrivacy().equals(Privacy.Public) && post.getUser().getId().equals(userId)) {

                    PostOutput postOutput = PostOutput.builder()
                            .id(post.getId())
                            .postName(post.getPostName())
                            .postType(post.getPostType())
                            .postData(post.getPostData().length)
                            .postUrl(post.getPostUrl())
                            .createDate(post.getCreateDate())
                            .postOwnerName(post.getUser().getUserName())
                            .build();
                    newList.add(postOutput);
                }
            }
            return new ResponseEntity<>(newList, HttpStatus.OK);
        }
        return new ResponseEntity<>(List.of("Account is Disabled "), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

    }

    private ResponseEntity<List<?>> getListResponseEntity(Long userId, ArrayList<PostOutput> newList) {

        for (Post post : postRepository.findByUserId(userId)) {
            PostOutput postOutput = PostOutput.builder()
                    .id(post.getId())
                    .postName(post.getPostName())
                    .postType(post.getPostType())
                    .postData(post.getPostData().length)
                    .postUrl(post.getPostUrl())
                    .createDate(post.getCreateDate())
                    .postOwnerName(post.getUser().getUserName())
                    .build();
            newList.add(postOutput);

        }

        return new ResponseEntity<>(newList, HttpStatus.OK);
    }


    public ResponseEntity<String> deletePost(Long postId, String username) {

        if (userService.isAccountEnable(username)) {
            if (postRepository.existsById(postId)) {
                Optional<User> user = userRepository.findByUserName(username);
                Long postOwnerId = postRepository.findById(postId).get().getUser().getId();
                if (postRepository.findById(postId).isEmpty()) {
                    return new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
                }
                String userRole = user.get().getRole();
                if (userRole.equals("ROLE_ADMIN")) {
                    postRepository.deleteById(postId);
                    return new ResponseEntity<>("Post is Successfully delete by admin " + user.get().getUserName(), HttpStatus.OK);
                }
                if (Objects.equals(postOwnerId, user.get().getId())) {
                    postRepository.deleteById(postId);
                    return new ResponseEntity<>("Post is Successfully delete by user " + user.get().getUserName(), HttpStatus.OK);
                }
                return new ResponseEntity<>("Post can only be delete by post owner or admin ", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Post not available with post id " + postId, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Account is Disabled ", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


    public ResponseEntity<String> reSharePost(Long postId, String username) {

        if (userService.isAccountEnable(username)) {
        if (postRepository.existsById(postId) && userRepository.findByUserName(username).isPresent()) {
            Optional<User> user = userRepository.findByUserName(username);
            Optional<Post> postInput = postRepository.findById(postId);
            if (postRepository.findById(postId).isEmpty()) {
                return new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
            }
            Privacy postPrivacy = postInput.get().getPrivacy();

            if (postPrivacy.equals(Privacy.Public)) {
                return getStringResponseEntityReShare(user, postInput);
            } else if (followService.isFollowing(username, user.get().getId())) {

                return getStringResponseEntityReShare(user, postInput);
            }
        }
        return new ResponseEntity<>("Failed to re share the post is private", HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
}

    private ResponseEntity<String> getStringResponseEntityReShare(Optional<User> user, Optional<Post> postInput) {
        Long count = postInput.get().getShareCount();
        postInput.get().setShareCount(count + 1L);
        postRepository.save(postInput.get());

        Post newPost = Post.builder()
                .postUrl(postInput.get().getPostUrl())
                .postName(postInput.get().getPostName())
                .postData(postInput.get().getPostData())
                .postType(postInput.get().getPostType())
                .privacy(postInput.get().getPrivacy())
                .shareCount(0L)
                .user(user.get())
                .build();
        postRepository.save(newPost);

        return new ResponseEntity<>("Post Re-Share Successfully", HttpStatus.OK);
    }

    //
    public ResponseEntity<String> reShareCount(Long postId, String username) {
        if (userService.isAccountEnable(username)) {
        if(postRepository.findById(postId).isEmpty()){
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Post Re-Share count are : "+ postRepository.findById(postId).get().getShareCount(),HttpStatus.OK);

    }
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    public ResponseEntity<byte[]> postById(Long postId) {

        Post post =postRepository.findById(postId).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + post.getPostName() + "\"")
                .body(post.getPostData());
    }

}
