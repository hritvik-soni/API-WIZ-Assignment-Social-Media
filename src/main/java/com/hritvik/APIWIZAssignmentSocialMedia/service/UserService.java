package com.hritvik.APIWIZAssignmentSocialMedia.service;


import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.EmailDetails;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.UserInput;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.UserUpdateInput;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;


    public ResponseEntity<String> createUser(UserInput userInput) {
        // checking username exist or not

        if(userRepository.findByUserName(userInput.getUserName()).isPresent()){
            return new ResponseEntity<>("UserName  Already exist please try with different details",HttpStatus.BAD_REQUEST);
        }
        // checking email exist or not
        if(userRepository.findByEmail(userInput.getEmail()).isPresent()){
            return new ResponseEntity<>("Email Already exist please try with different details",HttpStatus.BAD_REQUEST);
        }
        //Building user

        User user = User.builder()
                .name(userInput.getName())
                .userName(userInput.getUserName())
                .bio(userInput.getBio())
                .password(passwordEncoder.encode(userInput.getPassword()))
                .email(userInput.getEmail())
                .gender(userInput.getGender())
                .profileImage(userInput.getProfileImage())
                .role(userInput.getRole())
                .build();
   // saving user details in db
        User savedUser = userRepository.save(user);
        // building email details
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has been Successfully Created.\nYour Account Details: \n"
                        + "Name: " + savedUser.getName() +"\n"
                        +"Username: " + savedUser.getUserName()+"\n"
                        +"\n\n"
                        +"Cick on this link to Activate account \n"
                        +"http://localhost:8080/api/user/activate/"+ savedUser.getId() ) // activation link
                      .build();
        // sending mail
        emailService.sendEmailAlert(emailDetails);
        //returning result
        return new ResponseEntity<>("User Created Successfully and Activation link sent on mail" , HttpStatus.OK);
    }


    public User getUserById(Long userId) {
        //  logic for getting a user by ID
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAllUsers() {
        // logic for getting all users
        return userRepository.findAll();
    }


    public ResponseEntity<String> deleteUser(Long userId, String username) {
     // checking if account is Active or not
        if (isAccountEnable(username)) {
          // check if user ia admin
            if (userRepository.findByUserName(username).get().getRole().equals("ROLE_ADMIN")) {
                //deleting user
                userRepository.deleteById(userId);
                return new ResponseEntity<>("user removed successfully", HttpStatus.OK);
            }
            // check if login user and user id matches
            else if (userRepository.findByUserName(username).get().getId().equals(userId)) {
                //deleting user
                userRepository.deleteById(userId);
                return new ResponseEntity<>("user removed successfully", HttpStatus.OK);

            }
           // returning failure message
            return new ResponseEntity<>("Only Self-User or admin can remove user", HttpStatus.BAD_REQUEST);

        }
        // returning failure message
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    public ResponseEntity<String> Activate(Long userId){
            if (userRepository.existsById(userId)) {
                Optional<User> user = userRepository.findById(userId);
                user.get().setAccountActivate(true);
                userRepository.save(user.get());
                return new ResponseEntity<>("Account Activation successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Account Activation Failed", HttpStatus.BAD_REQUEST);
        }


    public ResponseEntity<String> updateUser( String username, UserUpdateInput updateInput) {
       // check for account active
        if(isAccountEnable(username)) {
            // searching for user
            Optional<User> user = userRepository.findByUserName(username);
             // updating details
            if (user.isPresent()) {
                if (updateInput.getBio() != null) {
                    user.get().setBio(updateInput.getBio());
                }
                if (updateInput.getProfileImage() != null) {
                    user.get().setProfileImage(updateInput.getProfileImage());
                }
                // saving user details
                userRepository.save(user.get());
               // return success
                return new ResponseEntity<>("User Details Updated Successfully", HttpStatus.OK);
            }
            // return failure
            return new ResponseEntity<>("Error occurred try again", HttpStatus.BAD_REQUEST);
        }
        //  // return failure
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    public boolean isAccountEnable (String username){
        // checking account active or not
        return userRepository.findByUserName(username).get().isAccountActivate();
    }

    public ResponseEntity<String> disableAccount(String username, Long userId) {
         // checking for user
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            // return failure
            return  new ResponseEntity<>("No user available wih user id "+ userId,HttpStatus.BAD_REQUEST);
        }
        // disabling account
        user.get().setAccountActivate(false);
        // saving user;
        userRepository.save(user.get());
        // return success
        return  new ResponseEntity<>("Account Disable successfully By Admin "+ username,HttpStatus.OK);

    }
}
