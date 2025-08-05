package com.adilabdullayev.psychology.repository.notes;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCounselorNoteRepository extends JpaRepository<UserCounselorNote, Long> {
    List<UserCounselorNote> findByPatientId(Long patientId);
    List<UserCounselorNote> findByPatientIdAndType(Long patientId, UserCounselorNote.NoteType type);
}
