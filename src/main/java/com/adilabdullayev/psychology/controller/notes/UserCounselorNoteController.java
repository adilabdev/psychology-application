package com.adilabdullayev.psychology.controller.notes;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.service.notes.UserCounselorNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/notes")
public class UserCounselorNoteController {

    private final UserCounselorNoteService noteService;

    public UserCounselorNoteController(UserCounselorNoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<UserCounselorNote> addNote(@PathVariable Long patientId,
                                                     @RequestBody UserCounselorNote note) {
        return ResponseEntity.ok(noteService.addNote(patientId, note));
    }

    @GetMapping
    public ResponseEntity<List<UserCounselorNote>> getAllNotes(@PathVariable Long patientId) {
        return ResponseEntity.ok(noteService.getAllNotes(patientId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<UserCounselorNote>> getNotesByType(
            @PathVariable Long patientId,
            @PathVariable UserCounselorNote.NoteType type) {
        return ResponseEntity.ok(noteService.getNotesByType(patientId, type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserCounselorNote>> searchNotes(
            @PathVariable Long patientId,
            @RequestParam String keyword,
            @RequestParam(required = false) UserCounselorNote.NoteType type) {

        List<UserCounselorNote> notes = noteService.searchNotesByKeyword(patientId, keyword, type);
        return ResponseEntity.ok(notes);
    }


}
