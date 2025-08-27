package com.adilabdullayev.psychology.service.notes;

import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.archived.ArchivedUserCounselorNote;  // Import for ArchivedUserCounselorNote
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;  // Import for ArchievedUserCounselorNoteRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;  // Import for Page
import org.springframework.data.domain.PageRequest;  // Import for PageRequest
import org.springframework.data.domain.Pageable;  // Import for Pageable
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArchivedUserCounselorNoteService {

    @Autowired
    private ArchivedUserCounselorNoteRepository archievedNoteRepository;

    // get archived notes for a patient with pagination
    public Page<ArchivedUserCounselorNote> getArchievedNotes(Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return archievedNoteRepository.findByArchivedPatientId(patientId, pageable);
    }

    // add new archieved note
    public ArchivedUserCounselorNote addArchievedNote(ArchivedUserCounselorNote note){
        return archievedNoteRepository.save(note);
    }

    // filters archived notes by type
    public List<ArchivedUserCounselorNote> getArchivedNotesByType(NoteType noteType) {
        return archievedNoteRepository.findByNoteType(noteType);
    }

    // brings archived notes visible to patient
    public List<ArchivedUserCounselorNote> getVisibleArchivedNotes() {
        return archievedNoteRepository.findByIsVisibleToPatientTrue();
    }

    // brings archived notes that are soft deleted
    public List<ArchivedUserCounselorNote> getDeletedArchivedNotes() {
        return archievedNoteRepository.findByDeletedAtIsNotNull();
    }

    // brings archived notes created after a specific date
    public List<ArchivedUserCounselorNote> getArchivedNotesCreatedAfter(LocalDateTime date) {
        return archievedNoteRepository.findByCreatedAtAfter(date);
    }

    // filters archived notes by type and visibility
    public List<ArchivedUserCounselorNote> getArchivedNotesByTypeAndVisibility(NoteType noteType, boolean visible) {
        return archievedNoteRepository.findByNoteTypeAndIsVisibleToPatient(noteType, visible);
    }
}

