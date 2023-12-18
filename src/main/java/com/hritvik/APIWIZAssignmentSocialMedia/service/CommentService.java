package com.hritvik.APIWIZAssignmentSocialMedia.service;

import com.hritvik.APIWIZAssignmentSocialMedia.model.Comment;
import com.hritvik.APIWIZAssignmentSocialMedia.model.Post;
import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Privacy;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.CommentRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.PostRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserService userService;


    public ResponseEntity<String> addComment(String username, Long postId, String comment) {
        // checking is user/account is active
        if (userService.isAccountEnable(username)) {
        Optional<User> user = userRepository.findByUserName(username);
        Optional<Post> post = postRepository.findById(postId);
         // checking post exist
        if(post.isEmpty()){
            // return failure
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        Privacy postPrivacy = post.get().getPrivacy();
        // checking is post is public
        if(postPrivacy.equals(Privacy.Public)){
            // building comment
            Comment newComment = Comment.builder()
                    .commentBody(comment)
                    .commenter(user.get())
                    .instaPost(post.get())
                    .build();
            // saving comment
            commentRepository.save(newComment);
            // return success
            return  new ResponseEntity<>("You have commented the post", HttpStatus.OK);
        }
        // checking is user a follower
        else if(followService.isFollowing(username,userRepository.findByUserName(username).get().getId())){
            // building comment
            Comment newComment = Comment.builder()
                    .commentBody(comment)
                    .commenter(user.get())
                    .instaPost(post.get())
                    .build();
//            saving comment
            commentRepository.save(newComment);
     // return success
            return  new ResponseEntity<>("You have commented the post", HttpStatus.OK);
        }
            // return failure
        return  new ResponseEntity<>("Invalid credentials",HttpStatus.BAD_REQUEST);

    }
        // return failure
     return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
}

    public ResponseEntity<String> removeComment(String username, Long postId, Long commentId) {
        // checking is user/account is active
        if (userService.isAccountEnable(username)) {
//        Optional<User> user = userRepository.findByUserName(username);
        Optional<Post> post = postRepository.findById(postId);
        Optional<Comment> comment = commentRepository.findById(commentId);
      // check if post exist
        if(post.isEmpty()){
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        // check if comment exist
        if(comment.isEmpty()){
            return  new ResponseEntity<>("Comment does not exist", HttpStatus.BAD_REQUEST);
        }
       // post owner remove comment
        if(post.get().getUser().getUserName().equals(username)){
            commentRepository.deleteById(commentId);
            return  new ResponseEntity<>("Comment Removed successfully",HttpStatus.OK);
        }

        // commenter removing comment
        if(comment.get().getCommenter().getUserName().equals(username)){
            commentRepository.deleteById(commentId);
            return  new ResponseEntity<>("Comment Removed successfully",HttpStatus.OK);
        }
    //   return failure
        return new ResponseEntity<>("only post owner or commenter can remove comment",HttpStatus.BAD_REQUEST);

    }
        //   return failure
     return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }

    public ResponseEntity<String> countComment(String username, Long postId) {
        if (userService.isAccountEnable(username)) {
//        Optional<User> user = userRepository.findByUserName(username);
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        Privacy postPrivacy = post.get().getPrivacy();
        int count = 0;
            // checking is post public and counting comments
        if (postPrivacy.equals(Privacy.Public)) {
            for (Comment comment : commentRepository.findAll()) {
                if (comment.getInstaPost().equals(post.get())) {
                    count++;
                }
            }
            // return success
            return new ResponseEntity<>("Post have" + count + "Comments", HttpStatus.OK);
        }
  // checking is user a follower and counting comments
        if (followService.isFollowing(username, post.get().getUser().getId())) {
            for (Comment comment : commentRepository.findAll()) {
                if (comment.getInstaPost().equals(post.get())) {
                    count++;
                }
            }
            //   return success
            return new ResponseEntity<>("Post have" + count + "comment", HttpStatus.OK);
        }
            //   return failure
        return new ResponseEntity<>("Post is private You cannot see the comment", HttpStatus.BAD_REQUEST);

    }
        //   return failure
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
}

