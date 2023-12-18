package com.hritvik.APIWIZAssignmentSocialMedia.controller;



import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.UserInput;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.UserUpdateInput;
import com.hritvik.APIWIZAssignmentSocialMedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Creating new user
     * @param userInput taking input as response body
     * @return returning success or failure message
     */
    @PostMapping("/new")
    public ResponseEntity<String> createUser(@RequestBody UserInput userInput) {
        return userService.createUser(userInput);
    }

    /**
     * Searching for specific user
     * @param userId taking postId as input
     * @return returning success or failure message
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Searching for all the users
     * @return returning success or failure message
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     *  Updating user detail
     * @param authentication * @param authentication using for extracting long info(username)
     * @param updateInput taking input for user detail update(profile pic or bio)
     * @return returning success or failure message
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_USER')")

    public ResponseEntity<String> updateUser(Authentication authentication, @RequestBody UserUpdateInput updateInput) {
          String username= authentication.getName();

        return userService.updateUser(username,updateInput);
    }

    /**
     * removing user
     * @param authentication using for extracting long info(username)
     * @param userId  taking userId as input
     * @return returning success or failure message
     */
    @DeleteMapping("/remove/{userId}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN,ROLE_USER')")
    public ResponseEntity<String> deleteUser(Authentication authentication,@PathVariable Long userId) {
        String username = authentication.getName();
      return  userService.deleteUser(userId,username);

    }

    /**
     * activating user account
     * @param userId  taking userId as input
     * @return returning success or failure message
     */

    @GetMapping("/activate/{userId}")
    public ResponseEntity<String> activate (@PathVariable Long userId) {
       return userService.Activate(userId);

    }

    /**
     * Disabling the user account
     * @param authentication using for extracting long info(username)
     * @param userId  taking userId as input
     * @return returning success or failure message
     */
    @PostMapping("/disable/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> disable(Authentication authentication,@PathVariable Long userId) {
        String username = authentication.getName();
        return userService.disableAccount(username,userId);

    }


}
