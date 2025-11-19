package com.nitin.secure_notes.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder

public class NoteResponse {
    private String id;
    private String title;
    private String content;}
