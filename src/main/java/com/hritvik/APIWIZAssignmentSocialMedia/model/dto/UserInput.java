package com.hritvik.APIWIZAssignmentSocialMedia.model.dto;

import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {

    @NotBlank
    private String name;
    @NotNull
    private String userName;
    @Email
    private String email;
    @NotNull
    private String password;
    private Gender gender;
    private String profileImage;
    private String bio;
    private String role;
}
