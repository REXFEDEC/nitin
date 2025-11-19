package com.nitin.secure_notes.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class LoginRequest {

    @Email(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
