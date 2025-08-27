package com.adilabdullayev.psychology.controller.notes;

import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.archived.ArchivedUserCounselorNote;
import com.adilabdullayev.psychology.service.notes.ArchivedUserCounselorNoteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/patients/{patientId}/archived-notes")
public class ArchivedUserCounselorNoteController {

    private final ArchivedUserCounselorNoteService archivedNoteService;

    public ArchivedUserCounselorNoteController(ArchivedUserCounselorNoteService archivedNoteService) {
        this.archivedNoteService = archivedNoteService;
    }

    // creates archived note for specific client
    @PostMapping
    public ResponseEntity<ArchivedUserCounselorNote> addArchivedNote(@RequestBody ArchivedUserCounselorNote note) {
        return ResponseEntity.ok(archivedNoteService.addArchievedNote(note));
    }

    // gets archived notes for specific client with pagination
    @GetMapping
    public ResponseEntity<?> getArchivedNotes(@PathVariable Long patientId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(archivedNoteService.getArchievedNotes(patientId, page, size));
    }

    // filters archived notes by type
    @GetMapping("/filter/type")
    public ResponseEntity<List<ArchivedUserCounselorNote>> getArchivedNotesByType(@RequestParam NoteType noteType) {
        return ResponseEntity.ok(archivedNoteService.getArchivedNotesByType(noteType));
    }

    // gets archived notes visible to patient
    @GetMapping("/visible")
    public ResponseEntity<List<ArchivedUserCounselorNote>> getVisibleArchivedNotes() {
        return ResponseEntity.ok(archivedNoteService.getVisibleArchivedNotes());
    }

    // gets archived notes that are soft deleted
    @GetMapping("/deleted")
    public ResponseEntity<List<ArchivedUserCounselorNote>> getDeletedArchivedNotes() {
        return ResponseEntity.ok(archivedNoteService.getDeletedArchivedNotes());
    }

    // gets archived notes created after a specific date
    @GetMapping("/created-after")
    public ResponseEntity<List<ArchivedUserCounselorNote>> getArchivedNotesCreatedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(archivedNoteService.getArchivedNotesCreatedAfter(date));
    }

    // filters archived notes by type and visibility
    @GetMapping("/filter")
    public ResponseEntity<List<ArchivedUserCounselorNote>> getArchivedNotesByTypeAndVisibility(
            @RequestParam NoteType noteType,
            @RequestParam boolean visible) {
        return ResponseEntity.ok(archivedNoteService.getArchivedNotesByTypeAndVisibility(noteType, visible));
    }
}
