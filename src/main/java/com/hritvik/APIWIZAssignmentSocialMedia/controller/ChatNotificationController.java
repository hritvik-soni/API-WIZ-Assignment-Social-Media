package com.hritvik.APIWIZAssignmentSocialMedia.controller;



import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.EmailDetails;
import com.hritvik.APIWIZAssignmentSocialMedia.model.dto.GroupChatInput;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.UserRepository;
import com.hritvik.APIWIZAssignmentSocialMedia.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatNotificationController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/group")
    public ResponseEntity<String> groupChat(Authentication authentication, @RequestBody GroupChatInput input) {
        String authUser = authentication.getName();
        for (String username : input.getUserNames()) {

            String email = userRepository.findByUserName(username).get().getEmail();

         if(email!=null) {
             EmailDetails emailDetails = EmailDetails.builder()
                     .recipient(email)
                     .subject("Group Chat Invite")
                     .messageBody("Hello ," + authUser + " invite you for group chat \n" +
                             "\n"
                             + "Click on this link to start chat (please enter username and password)\n"
                             + "http://localhost:8080/")
                     .build();
             emailService.sendEmailAlert(emailDetails);
         }
        }
        return new ResponseEntity<>("Group invite sent to all the users please visit this link to start group chat\n"+
                "http://localhost:8080/"+"\n"+"(please enter username and password)", HttpStatus.OK);
    }

    @GetMapping("/1v1")
    public ResponseEntity<String> onev1Chat(Authentication authentication, @RequestParam("username") String username) {
        String authUser = authentication.getName();

            String email = userRepository.findByUserName(username).get().getEmail();
        if(email==null){
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(email)
                    .subject("Group Chat Invite")
                    .messageBody("Hello ," + authUser + " invite you for 1v1 chat \n" +
                            "\n"
                            + "Click on this link to start chat (please enter username and password)\n"
                            + "http://localhost:8080/")
                    .build();
            emailService.sendEmailAlert(emailDetails);

        return new ResponseEntity<>("Group invite sent to all the users please visit this link to start group chat\n"+
                "http://localhost:8080/"+"\n"+"(please enter username and password)", HttpStatus.OK);
    }





}
