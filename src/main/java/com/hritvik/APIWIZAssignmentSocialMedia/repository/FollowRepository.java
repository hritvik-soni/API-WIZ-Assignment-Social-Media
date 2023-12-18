package com.hritvik.APIWIZAssignmentSocialMedia.repository;

import com.hritvik.APIWIZAssignmentSocialMedia.model.Follow;
import com.hritvik.APIWIZAssignmentSocialMedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByCurrentUserAndCurrentUserFollower(User targetUser, User follower);
}
