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
        // checking is user/account is active
        if (userService.isAccountEnable(username)) {
        Optional<User> followUser = userRepository.findByUserName(username);
      // checking if already a follower
        if (isFollowing(myUserName, userRepository.findByUserName(username).get().getId())) {
            // return failure
            return new ResponseEntity<>("your are already following the user", HttpStatus.BAD_REQUEST);
        }
       // building follower
        Follow follow = Follow.builder()
                .currentUser(userRepository.findByUserName(username).get())
                .currentUserFollower(userRepository.findByUserName(myUserName).get())
                .build();
//        saving follow
        followRepository.save(follow);
//         return success
        return new ResponseEntity<>("You are successfully following " + username, HttpStatus.OK);

    }
//         return Failure
      return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
}

    public ResponseEntity<String> unFollow(String myUserName, String username) {
        // checking is user/account is active
        if (userService.isAccountEnable(username)) {
         // calling unfollow method
        if (unfollowmethod(myUserName, username))
            // return success
            return new ResponseEntity<>("user successfully unfollowed", HttpStatus.OK);

        if(isFollowing(myUserName,userRepository.findByUserName(username).get().getId())){
            // calling unfollow method
            if (unfollowmethod(myUserName, username))
                // return success
                return new ResponseEntity<>("user successfully unfollowed", HttpStatus.OK);
        }
            // return failure
        return new ResponseEntity<>("You are not following the user",HttpStatus.BAD_REQUEST);

    }
        // return failure
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    private boolean unfollowmethod(String myUserName, String username) {
      // removing follower
        Long followId;
        // searching for follower id
        for(Follow follow : followRepository.findAll()){

            if(follow.getCurrentUserFollower().getUserName().equals(myUserName)
                    && follow.getCurrentUser().getUserName().equals(username)){
                followId= follow.getFollowId();
                // removing follow
                followRepository.deleteById(followId);
//              return success
                return true;

            }
        }
        // return failure
        return false;
    }


    public boolean isFollowing(String username, Long userId) {
         // checking is user a follower for other user
        Optional<User> user = userRepository.findById(userId);

        for(Follow follow : followRepository.findAll()){

            if(follow.getCurrentUserFollower().getUserName().equals(username)
                    && follow.getCurrentUser().equals(user.get())){
                // return success
                return true;
             }
            }
        // return failure
        return false;
    }
}