package com.hritvik.APIWIZAssignmentSocialMedia.service;

import com.hritvik.APIWIZAssignmentSocialMedia.model.Like;
import com.hritvik.APIWIZAssignmentSocialMedia.model.Post;
import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Privacy;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.LikeRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.PostRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FollowService followService;
    @Autowired
   private UserRepository userRepository;
    @Autowired
    UserService userService;

    public ResponseEntity<String> addLike(String username, Long postId) {
        if (userService.isAccountEnable(username)) {
        Optional<Post> post =  postRepository.findById(postId);
        if(post.isEmpty()){
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }

        Privacy postPrivacy = post.get().getPrivacy();

        if(postPrivacy.equals(Privacy.Public)){

            Like like = Like.builder()
                    .instaPost(post.get())
                    .liker(userRepository.findByUserName(username).get())
                    .build();
            likeRepository.save(like);

            return  new ResponseEntity<>("You have Liked the post", HttpStatus.OK);
        }
        else if(followService.isFollowing(username,userRepository.findByUserName(username).get().getId())){
            Like like = Like.builder()
                    .instaPost(post.get())
                    .liker(userRepository.findByUserName(username).get())
                    .build();
            likeRepository.save(like);

            return  new ResponseEntity<>("You have Liked the post", HttpStatus.OK);
        }

        return new ResponseEntity<>("Can not like a private post",HttpStatus.BAD_REQUEST);

    }
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


    public ResponseEntity<String> removeLike(String username, Long postId) {

        if (userService.isAccountEnable(username)) {

        Optional<User> user = userRepository.findByUserName(username);
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        Long likeId=0L;
        String likerUsername="";

        for (Like like : likeRepository.findAll()){
            if(like.getInstaPost().equals(post.get()) && like.getLiker().equals(user.get())){
                likeId=like.getLikeId();
                likerUsername = like.getLiker().getUserName();
            }
        }
        if(likeId==null){
            return new ResponseEntity<>("You can not remove the like as you have not liked the post",HttpStatus.BAD_REQUEST);
        }

        if(likerUsername.equals(username)) {
            likeRepository.deleteById(likeId);
            return new ResponseEntity<>("You have successfully remove the Like",HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials",HttpStatus.BAD_REQUEST);

    }
     return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
}

    public ResponseEntity<String> countLike(String username, Long postId) {
        if (userService.isAccountEnable(username)) {
//        Optional<User> user = userRepository.findByUserName(username);
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            return  new ResponseEntity<>("Post does not exist", HttpStatus.BAD_REQUEST);
        }
        Privacy postPrivacy = post.get().getPrivacy();
        int count=0;

        if(postPrivacy.equals(Privacy.Public)){
            for(Like like : likeRepository.findAll()){
                if(like.getInstaPost().equals(post.get())){
                    count++;
                }
            }
            return new ResponseEntity<>("Post have"+count+"Likes",HttpStatus.OK);
        }

        if(followService.isFollowing(username,post.get().getUser().getId())){
            for(Like like : likeRepository.findAll()){
                if(like.getInstaPost().equals(post.get())){
                    count++;
                }
            }
            return new ResponseEntity<>("Post have"+count+"Likes",HttpStatus.OK);
        }

        return  new ResponseEntity<>("Post is private You cannot see the likes",HttpStatus.BAD_REQUEST);

    }
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}
