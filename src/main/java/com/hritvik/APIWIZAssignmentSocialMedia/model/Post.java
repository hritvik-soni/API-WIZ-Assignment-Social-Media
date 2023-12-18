package com.hritvik.APIWIZAssignmentSocialMedia.model;


import com.hritvik.APIWIZAssignmentSocialMedia.model.enums.Privacy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String content; // Caption
    private String postName;

    private String postType;
    @Lob
    private byte[] postData;

    private String postUrl;

    private Privacy privacy; // "public" or "private"

    private Long shareCount=0L;

    @CreationTimestamp
    private LocalDateTime createDate; // creation time stamp

    @ManyToOne
    @JoinColumn(name = "post_users_id")
    private User user;

}
