package com.hritvik.APIWIZAssignmentSocialMedia.model.dto;


import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.PostType;
import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Privacy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInput {

    private String content;
    private PostType postType;
    private String postUrl;
    private Privacy privacy; // "public" or "private"


}
