package com.adilabdullayev.psychology.repository.notes;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.notes.NoteOwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCounselorNoteRepository extends JpaRepository<UserCounselorNote, Long> {

    // brings notes for specific client
    List<UserCounselorNote> findByPatientId(Long patientId);

    // filters notes for a specific client by ownership type
    List<UserCounselorNote> findByPatientIdAndNoteOwnerType(Long patientId, NoteOwnerType noteOwnerType);

    // case-intensive search on the notes keyword based
    List<UserCounselorNote> findByPatientIdAndContentContainingIgnoreCase(Long patientId, String keyword);

    // filters notes by each ownership type and keyword
    List<UserCounselorNote> findByPatientIdAndNoteOwnerTypeAndContentContainingIgnoreCase(Long patientId, NoteOwnerType noteOwnerType, String keyword);

}