package com.adilabdullayev.psychology.service.notes;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.model.notes.NoteOwnerType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCounselorNoteService {

    private final UserCounselorNoteRepository noteRepository;
    private final PatientRepository patientRepository;

    public UserCounselorNoteService(UserCounselorNoteRepository noteRepository, PatientRepository patientRepository) {
        this.noteRepository = noteRepository;
        this.patientRepository = patientRepository;
    }

    // adds a note for a client by checking the client
    public UserCounselorNote addNote(Long patientId, UserCounselorNote note) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));

        note.setPatient(patient);
        return noteRepository.save(note);
    }

    // brings all notes by specific client
    public List<UserCounselorNote> getAllNotes(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    // brings notes by ownership type fpr a specific client
    public List<UserCounselorNote> getNotesByOwnerType(Long patientId, NoteOwnerType noteOwnerType) {
        return noteRepository.findByPatientIdAndNoteOwnerType(patientId, noteOwnerType);
    }

    // makes search by a keyword at client notes, u can do ownership(NoteOwnerType) gilter as optional choice
    public List<UserCounselorNote> searchNotesByKeyword(Long patientId, String keyword, NoteOwnerType noteOwnerType) {
        if (noteOwnerType == null) {
            return noteRepository.findByPatientIdAndContentContainingIgnoreCase(patientId, keyword);
        } else {
            return noteRepository.findByPatientIdAndNoteOwnerTypeAndContentContainingIgnoreCase(patientId, noteOwnerType, keyword);
        }
    }
    // GET http://localhost:8080/patients/{patientId}/notes/search?keyword=depresyon url for testing both counselor and patient
    // GET /patients/{patientId}/notes/search?keyword=depresyon&type=USER only patient (http://localhost:8080/patients/4/notes/search?keyword=depresyon&type=USER)
    // GET /patients/{patientId}/notes/search?keyword=depresyon&type=COUNSELOR only counselor(http://localhost:8080/patients/4/notes/search?keyword=depresyon&type=COUNSELOR)

}
