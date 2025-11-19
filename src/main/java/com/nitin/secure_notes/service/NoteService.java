package com.nitin.secure_notes.service;

import com.nitin.secure_notes.dto.NoteRequest;
import com.nitin.secure_notes.dto.NoteResponse;
import com.nitin.secure_notes.model.Note;
import com.nitin.secure_notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteResponse createNote(NoteRequest request, String name) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // 🎯 Logged-in user

        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .userEmail(email)
                .build();

        note = noteRepository.save(note);

        return mapToResponse(note);
    }

    public List<NoteResponse> getMyNotes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return noteRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }

    public NoteResponse updateNote(String id, NoteRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        note = noteRepository.save(note);

        return mapToResponse(note);
    }

    private NoteResponse mapToResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }
}
