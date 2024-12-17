package me.toparang.blog.dto;

import lombok.*;

@Setter
@Getter
public class AddUserRequest {
    private String email;
    private String password;
}
