package com.nitin.secure_notes.controller;

import com.nitin.secure_notes.dto.NoteRequest;
import com.nitin.secure_notes.dto.NoteResponse;
import com.nitin.secure_notes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request, Principal principal) {
        System.out.println(" Principal: " + principal);
        System.out.println(" Principal Name (email): " + principal.getName());

        return ResponseEntity.ok(noteService.createNote(request, principal.getName()));
    }


    @GetMapping("/fetch")
    public ResponseEntity<List<NoteResponse>> getNotes() {
        return ResponseEntity.ok(noteService.getMyNotes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(@PathVariable String id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}
