package com.hritvik.APIWIZAssignmentSocialMedia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostOutput {

    private Long id;
    private String postName;
    private String postType;
    private String postUrl;
    private int postData;
    private LocalDateTime createDate;
    private String postOwnerName;
}
