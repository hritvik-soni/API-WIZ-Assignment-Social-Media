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
       // checking for account active

        if (userService.isAccountEnable(username)) {
            //  logic for creating a post
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

             // building post
            Post post = Post.builder()
                    .postName(fileName)
                    .postData(file.getBytes())
                    .postType(file.getContentType())
                    .privacy(privacy)
                    .shareCount(0L)
                    .user(userRepository.findByUserName(username).get())
                    .build();

//           saving post
            postRepository.save(post);
            // creating url for download link
            String url = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/post/by/")
                    .path(String.valueOf(post.getId()))
                    .toUriString();
            post.setPostUrl(url);
            // saving post and updating url
            postRepository.save(post);
            // return success
            return new ResponseEntity<>("Post Created successfully", HttpStatus.OK);
        }
//         return failure
        return new ResponseEntity<>("Account is Disabled ", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


    public ResponseEntity<List<?>> getPostsByUserId(Long userId, String username) {
       // checking for account activate or not
        if (userService.isAccountEnable(username)) {

            ArrayList<PostOutput> newList = new ArrayList<PostOutput>();
            Optional<User> user = userRepository.findByUserName(username);


            String userRole = user.get().getRole();
             // checking for admin user
            if (userRole.equals("ROLE_ADMIN")) {
                return getListResponseEntity(userId, newList);
            }
            // checking for user equal login user
            if (Objects.equals(userId, user.get().getId())) {
                return getListResponseEntity(userId, newList);
            }
            // checking if user is follower or not
            if (followService.isFollowing(username, userId)) {
                return getListResponseEntity(userId, newList);
            }

            // searching for public posts
            for (Post post : postRepository.findAll()) {
                if (post.getPrivacy().equals(Privacy.Public) && post.getUser().getId().equals(userId)) {
                    // building postOutput details for list
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
            // return success
            return new ResponseEntity<>(newList, HttpStatus.OK);
        }
        // return failure
        return new ResponseEntity<>(List.of("Account is Disabled "), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

    }

    private ResponseEntity<List<?>> getListResponseEntity(Long userId, ArrayList<PostOutput> newList) {
      // searching for posts created by user
        for (Post post : postRepository.findByUserId(userId)) {
            // building post return
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
       // returning list of posts
        return new ResponseEntity<>(newList, HttpStatus.OK);
    }


    public ResponseEntity<String> deletePost(Long postId, String username) {
          // checking for account active
        if (userService.isAccountEnable(username)) {
            // checking post exist
            if (postRepository.existsById(postId)) {
                Optional<User> user = userRepository.findByUserName(username);
                Long postOwnerId = postRepository.findById(postId).get().getUser().getId();

//                if (postRepository.findById(postId).isEmpty()) {
//                    return new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
//                }
                String userRole = user.get().getRole();
                // checking for admin
                if (userRole.equals("ROLE_ADMIN")) {
                    // deleting post
                    postRepository.deleteById(postId);
                    // return success
                    return new ResponseEntity<>("Post is Successfully delete by admin " + user.get().getUserName(), HttpStatus.OK);
                }
                // checking for post owner
                if (Objects.equals(postOwnerId, user.get().getId())) {
                    // deleting post
                    postRepository.deleteById(postId);
                    // return success
                    return new ResponseEntity<>("Post is Successfully delete by user " + user.get().getUserName(), HttpStatus.OK);
                }
                // return failure
                return new ResponseEntity<>("Post can only be delete by post owner or admin ", HttpStatus.BAD_REQUEST);
            }
            // return failure
            return new ResponseEntity<>("Post not available with post id " + postId, HttpStatus.BAD_REQUEST);
        }
        // return failure
        return new ResponseEntity<>("Account is Disabled ", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


    public ResponseEntity<String> reSharePost(Long postId, String username) {
        // checking for account active
        if (userService.isAccountEnable(username)) {
            // checking for post
            if (postRepository.findById(postId).isEmpty()) {
                return new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
            }
            // checking for user
        if (userRepository.findByUserName(username).isPresent()) {
            Optional<User> user = userRepository.findByUserName(username);
            Optional<Post> postInput = postRepository.findById(postId);

            Privacy postPrivacy = postInput.get().getPrivacy();
       // checking if post is public
            if (postPrivacy.equals(Privacy.Public)) {
                return getStringResponseEntityReShare(user, postInput);
            }
            // checking user is a follower or not
            else if (followService.isFollowing(username, user.get().getId())) {

                return getStringResponseEntityReShare(user, postInput);
            }
        }
        // return failure
        return new ResponseEntity<>("Failed to re share the post is private", HttpStatus.BAD_REQUEST);
    }
        // return failure
    return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
}

    private ResponseEntity<String> getStringResponseEntityReShare(Optional<User> user, Optional<Post> postInput) {
       // updating post reShare count
        Long count = postInput.get().getShareCount();
        postInput.get().setShareCount(count + 1L);
        postRepository.save(postInput.get());
      // building post
        Post newPost = Post.builder()
                .postUrl(postInput.get().getPostUrl())
                .postName(postInput.get().getPostName())
                .postData(postInput.get().getPostData())
                .postType(postInput.get().getPostType())
                .privacy(postInput.get().getPrivacy())
                .shareCount(0L)
                .user(user.get())
                .build();
        // saving post
        postRepository.save(newPost);
  // return success
        return new ResponseEntity<>("Post Re-Share Successfully", HttpStatus.OK);
    }


    public ResponseEntity<String> reShareCount(Long postId, String username) {
        // checking for account active
        if (userService.isAccountEnable(username)) {
            // checking account exist
        if(postRepository.findById(postId).isEmpty()){
            // return failure
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        // return success
        return new ResponseEntity<>("Post Re-Share count are : "+ postRepository.findById(postId).get().getShareCount(),HttpStatus.OK);

    }
//        return failure
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    public ResponseEntity<byte[]> postById(Long postId) {
       // returning specifics post using id
        Post post =postRepository.findById(postId).get();
     // return post
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + post.getPostName() + "\"")
                .body(post.getPostData());
    }

}
