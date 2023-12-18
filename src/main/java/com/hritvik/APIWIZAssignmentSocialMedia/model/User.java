package com.hritvik.APIWIZAssignmentSocialMedia.model;

;
import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private Gender gender;
    private String profileImage;
    private String password;
    private String role;
    private String bio;
    private boolean isAccountActivate = false;

}