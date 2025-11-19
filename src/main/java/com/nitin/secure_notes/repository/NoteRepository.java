package com.nitin.secure_notes.repository;
import com.nitin.secure_notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByUserEmail(String email); // 🔍 Custom query
}
