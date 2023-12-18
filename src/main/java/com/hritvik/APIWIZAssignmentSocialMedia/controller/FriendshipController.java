package com.hritvik.APIWIZAssignmentSocialMedia.controller;//package com.hritvik.ApiWizAssignmentSocialMedia.controller;
//
//
//import com.hritvik.ApiWizAssignmentSocialMedia.service.FriendshipService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/friendships")
//public class FriendshipController {
//
//    @Autowired
//    private FriendshipService friendshipService;
//
//    @PostMapping("/send-request")
//    public ResponseEntity<Void> sendFriendRequest(@RequestParam Long userId, @RequestParam Long friendId) {
//        friendshipService.sendFriendRequest(userId, friendId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/accept-request/{friendshipId}")
//    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long friendshipId) {
//        friendshipService.acceptFriendRequest(friendshipId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/unfriend/{friendshipId}")
//    public ResponseEntity<Void> unfriend(@PathVariable Long friendshipId) {
//        friendshipService.unfriend(friendshipId);
//        return ResponseEntity.ok().build();
//    }
//}
