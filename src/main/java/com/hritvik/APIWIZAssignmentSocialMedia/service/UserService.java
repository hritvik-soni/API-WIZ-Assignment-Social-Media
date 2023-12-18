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

        if(userRepository.findByUserName(userInput.getUserName()).isPresent()){
            return new ResponseEntity<>("UserName  Already exist please try with different details",HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByEmail(userInput.getEmail()).isPresent()){
            return new ResponseEntity<>("Email Already exist please try with different details",HttpStatus.BAD_REQUEST);
        }


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

        User savedUser = userRepository.save(user);
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
        emailService.sendEmailAlert(emailDetails);
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

        if (isAccountEnable(username)) {

            if (userRepository.findByUserName(username).get().getRole().equals("ROLE_ADMIN")) {
                userRepository.deleteById(userId);
                return new ResponseEntity<>("user removed successfully", HttpStatus.OK);
            } else if (userRepository.findByUserName(username).get().getId().equals(userId)) {
                userRepository.deleteById(userId);
                return new ResponseEntity<>("user removed successfully", HttpStatus.OK);

            }

            return new ResponseEntity<>("Only Self-User or admin can remove user", HttpStatus.BAD_REQUEST);

        }
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

        if(isAccountEnable(username)) {

            Optional<User> user = userRepository.findByUserName(username);

            if (user.isPresent()) {
                if (updateInput.getBio() != null) {
                    user.get().setBio(updateInput.getBio());
                }
                if (updateInput.getProfileImage() != null) {
                    user.get().setProfileImage(updateInput.getProfileImage());
                }
                userRepository.save(user.get());

                return new ResponseEntity<>("User Details Updated Successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Error occurred try again", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Account is Disabled ",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    public boolean isAccountEnable (String username){
        return userRepository.findByUserName(username).get().isAccountActivate();
    }

    public ResponseEntity<String> disableAccount(String username, Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            return  new ResponseEntity<>("No user available wih user id "+ userId,HttpStatus.BAD_REQUEST);
        }
        user.get().setAccountActivate(false);
        userRepository.save(user.get());
        return  new ResponseEntity<>("Account Disable successfully By Admin "+ username,HttpStatus.OK);

    }
}
