package com.hritvik.APIWIZAssignmentSocialMedia.service;


import com.hritvik.APIWIZAssignmentSocialMedia.model.Follow;
import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.FollowRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;
    @Autowired
   private UserRepository userRepository;
    @Autowired
    UserService userService;


    public ResponseEntity<String> createFollow(String myUserName, String username) {
        if (userService.isAccountEnable(username)) {
        Optional<User> followUser = userRepository.findByUserName(username);

        if (isFollowing(myUserName, userRepository.findByUserName(username).get().getId())) {
            return new ResponseEntity<>("your are already following the user", HttpStatus.BAD_REQUEST);
        }

        Follow follow = Follow.builder()
                .currentUser(userRepository.findByUserName(username).get())
                .currentUserFollower(userRepository.findByUserName(myUserName).get())
                .build();
        followRepository.save(follow);

        return new ResponseEntity<>("You are successfully following " + username, HttpStatus.OK);

    }
      return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
}

    public ResponseEntity<String> unFollow(String myUserName, String username) {
        if (userService.isAccountEnable(username)) {

        if (unfollowmethod(myUserName, username))
            return new ResponseEntity<>("user successfully unfollowed", HttpStatus.OK);

        if(isFollowing(myUserName,userRepository.findByUserName(username).get().getId())){
            if (unfollowmethod(myUserName, username))
                return new ResponseEntity<>("user successfully unfollowed", HttpStatus.OK);
        }

        return new ResponseEntity<>("You are not following the user",HttpStatus.BAD_REQUEST);

    }
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    private boolean unfollowmethod(String myUserName, String username) {

        Long followId;
        for(Follow follow : followRepository.findAll()){

            if(follow.getCurrentUserFollower().getUserName().equals(myUserName)
                    && follow.getCurrentUser().getUserName().equals(username)){
                followId= follow.getFollowId();
                followRepository.deleteById(followId);

                return true;

            }
        }
        return false;
    }


    public boolean isFollowing(String username, Long userId) {

        Optional<User> user = userRepository.findById(userId);

        for(Follow follow : followRepository.findAll()){

            if(follow.getCurrentUserFollower().getUserName().equals(username)
                    && follow.getCurrentUser().equals(user.get())){
                return true;
             }
            }

        return false;
    }
}