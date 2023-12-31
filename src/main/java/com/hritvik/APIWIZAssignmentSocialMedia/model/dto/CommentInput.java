package com.hritvik.APIWIZAssignmentSocialMedia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentInput {

    private Long postId;
    private Long commentId;
    private String comment;
}
