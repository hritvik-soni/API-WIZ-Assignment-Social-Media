package com.hritvik.APIWIZAssignmentSocialMedia.repository;

import com.hritvik.APIWIZAssignmentSocialMedia.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
}
