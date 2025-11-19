package com.nitin.secure_notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {   @Id
private String id;

    private String title;
    private String content;
    private String userEmail;

}
