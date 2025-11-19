package com.nitin.secure_notes.dto;


import lombok.Data;

@Data
public class NoteRequest {
    private String title;
    private String content;
}
