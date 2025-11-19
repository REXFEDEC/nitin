package com.nitin.secure_notes.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "users")
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> roles;
}