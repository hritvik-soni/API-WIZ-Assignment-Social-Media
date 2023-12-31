package com.hritvik.APIWIZAssignmentSocialMedia.service;



import com.hritvik.APIWIZAssignmentSocialMedia.model.Friendship;
import com.hritvik.APIWIZAssignmentSocialMedia.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {

    @Autowired
   private FriendshipRepository friendshipRepository;

    public ResponseEntity<String> sendFriendRequest(String myUserName, String friendUserName) {


        // logic for sending a friend request

        for(Friendship friendship : friendshipRepository.findAll()){
            if(friendship.getRequestToUser().equals(friendUserName) && friendship.getRequestFromUser().equals(myUserName)){
                return new ResponseEntity<>("Friend Request already sent please wait for user to accept",HttpStatus.BAD_REQUEST);
            }
        }
        Friendship friendship =  Friendship.builder()
                .requestToUser(friendUserName)
                .requestFromUser(myUserName)
                .build();
        friendshipRepository.save(friendship);
        return new ResponseEntity<>("Friend Request Sent Successfully", HttpStatus.OK);
    }


    public Friendship acceptFriendRequest(Long friendshipId) {
        // Implement your logic for accepting a friend request
        if(friendshipRepository.existsById(friendshipId)){
            return  friendshipRepository.findById(friendshipId).get();
        }

        return null;


    }
    public void deleteRequest(Long friendshipId) {
        friendshipRepository.deleteById(friendshipId);
    }
}
