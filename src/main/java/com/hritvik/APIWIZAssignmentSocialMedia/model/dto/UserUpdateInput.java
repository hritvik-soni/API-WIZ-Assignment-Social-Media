package com.hritvik.APIWIZAssignmentSocialMedia.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateInput {

    private String profileImage;
    private String bio;
}
