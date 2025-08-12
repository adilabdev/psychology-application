package com.adilabdullayev.psychology.repository.notes;

import com.adilabdullayev.psychology.model.notes.ArchivedUserCounselorNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivedUserCounselorNoteRepository extends JpaRepository<ArchivedUserCounselorNote, Long> {

    // brings notes for specific client
    List<ArchivedUserCounselorNote> findByArchivedPatientId(Long archivedPatientId);

    // brings notes by note owner email address or id
    List<ArchivedUserCounselorNote> findByNoteOwnerEmailOrNoteOwnerId(String email, Long noteOwnerId);

    // deletes notes by note owner id or email
    void deleteByNoteOwnerEmailOrNoteOwnerId(String email, Long noteOwnerId);

}