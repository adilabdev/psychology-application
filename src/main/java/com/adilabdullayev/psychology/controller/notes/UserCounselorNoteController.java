package com.adilabdullayev.psychology.controller.notes;

import com.adilabdullayev.psychology.model.enums.NoteOwnerType;
import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.service.notes.UserCounselorNoteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    // gets all counselor notes for specific client
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
    @GetMapping("/search")
    public ResponseEntity<List<UserCounselorNote>> searchNotes(
            @PathVariable Long patientId,
            @RequestParam String keyword,
            @RequestParam(required = false) NoteOwnerType noteOwnerType) {
        List<UserCounselorNote> notes = noteService.searchNotesByKeyword(patientId, keyword, noteOwnerType);
        return ResponseEntity.ok(notes);
    }

    // gets notes by note type
    @GetMapping("/filter/type")
    public ResponseEntity<List<UserCounselorNote>> getNotesByType(@RequestParam NoteType noteType) {
        return ResponseEntity.ok(noteService.getNotesByType(noteType));
    }

    // gets notes visible to patient
    @GetMapping("/visible")
    public ResponseEntity<List<UserCounselorNote>> getVisibleNotes() {
        return ResponseEntity.ok(noteService.getVisibleNotes());
    }

    // gets soft-deleted notes
    @GetMapping("/deleted")
    public ResponseEntity<List<UserCounselorNote>> getDeletedNotes() {
        return ResponseEntity.ok(noteService.getDeletedNotes());
    }

    // gets notes created after a specific date
    @GetMapping("/created-after")
    public ResponseEntity<List<UserCounselorNote>> getNotesCreatedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(noteService.getNotesCreatedAfter(date));
    }

    // filters notes by type and visibility
    @GetMapping("/filter")
    public ResponseEntity<List<UserCounselorNote>> getNotesByTypeAndVisibility(
            @RequestParam NoteType noteType,
            @RequestParam boolean visible) {
        return ResponseEntity.ok(noteService.getNotesByTypeAndVisibility(noteType, visible));
    }
}
