package com.adilabdullayev.psychology.service.notes;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.repository.PatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
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

    public UserCounselorNote addNote(Long patientId, UserCounselorNote note) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));

        note.setPatient(patient);
        return noteRepository.save(note);
    }

    public List<UserCounselorNote> getAllNotes(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    public List<UserCounselorNote> getNotesByType(Long patientId, UserCounselorNote.NoteType type) {
        return noteRepository.findByPatientIdAndType(patientId, type);
    }

    public List<UserCounselorNote> searchNotesByKeyword(Long patientId, String keyword, UserCounselorNote.NoteType type) {
        if (type == null) {
            return noteRepository.findByPatientIdAndContentContainingIgnoreCase(patientId, keyword);
        } else {
            return noteRepository.findByPatientIdAndTypeAndContentContainingIgnoreCase(patientId, type, keyword);
        }
    }
    // GET http://localhost:8080/patients/{patientId}/notes/search?keyword=depresyon url for testing both counselor and patient
    // GET /patients/{patientId}/notes/search?keyword=depresyon&type=USER only patient (http://localhost:8080/patients/4/notes/search?keyword=depresyon&type=USER)
    // GET /patients/{patientId}/notes/search?keyword=depresyon&type=COUNSELOR only counselor(http://localhost:8080/patients/4/notes/search?keyword=depresyon&type=COUNSELOR)

}
