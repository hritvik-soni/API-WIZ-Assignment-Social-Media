package com.hritvik.APIWIZAssignmentSocialMedia.controller;



import com.hritvik.APIWIZAssignmentSocialMedia.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
   private FollowService followService;

    /**
     * creating follow for user
     * @param authentication using for extracting long info(username)
     * @param username username of user you want to follow
     * @return returning message for success of failure
     */

    @PostMapping("/new/{username}")
    public ResponseEntity<String> createFollow (Authentication authentication ,
                                                @PathVariable String username){
        String myUserName = authentication.getName();

        return followService.createFollow(myUserName,username);

    }

    /**
     * removing follow from the user
     * @param authentication using for extracting long info(username)
     * @param username username of user you want to follow
     * @return returning message for success of failure
     */

    @DeleteMapping("/unfollow/{username}")
    public ResponseEntity<String> unFollow (Authentication authentication ,
                                                @PathVariable String username){
        String myUserName = authentication.getName();

        return followService.unFollow(myUserName,username);

    }

//    @GetMapping("/isFollow/{userId}")
//    public boolean isFollow(Authentication authentication,@PathVariable Long userId){
//        String username = authentication.getName();
//        return followService.isFollowing(username,userId);
//    }

}
