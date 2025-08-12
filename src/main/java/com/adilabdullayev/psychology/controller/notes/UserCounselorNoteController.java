package com.adilabdullayev.psychology.controller.notes;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.service.notes.UserCounselorNoteService;
import com.adilabdullayev.psychology.model.notes.NoteOwnerType;
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

    // creates note for specific client
    @PostMapping
    public ResponseEntity<UserCounselorNote> addNote(@PathVariable Long patientId,
                                                     @RequestBody UserCounselorNote note) {
        return ResponseEntity.ok(noteService.addNote(patientId, note));
    }

    // gets all counselor notes fors specific client
    @GetMapping
    public ResponseEntity<List<UserCounselorNote>> getAllNotes(@PathVariable Long patientId) {
        return ResponseEntity.ok(noteService.getAllNotes(patientId));
    }

    // filters notes for owner types
    @GetMapping("/type/{noteOwnerType}")
    public ResponseEntity<List<UserCounselorNote>> getNotesByOwnerType(
            @PathVariable Long patientId,
            @PathVariable NoteOwnerType noteOwnerType) {
        return ResponseEntity.ok(noteService.getNotesByOwnerType(patientId, noteOwnerType));
    }

    // search with keyword (if u want u can filter it by owner type)
    // u can filter for not type as optional choice
    @GetMapping("/search")
    public ResponseEntity<List<UserCounselorNote>> searchNotes(
            @PathVariable Long patientId,
            @RequestParam String keyword,
            @RequestParam(required = false) NoteOwnerType noteOwnerType) {

        List<UserCounselorNote> notes = noteService.searchNotesByKeyword(patientId, keyword, noteOwnerType);
        return ResponseEntity.ok(notes);
    }


}
