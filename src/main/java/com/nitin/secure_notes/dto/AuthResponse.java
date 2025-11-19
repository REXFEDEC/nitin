package com.nitin.secure_notes.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuthResponse {
    private String token;
    private String message;
}
