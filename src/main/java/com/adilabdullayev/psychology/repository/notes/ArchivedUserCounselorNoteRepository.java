package com.adilabdullayev.psychology.repository.notes;

import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.archived.ArchivedUserCounselorNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArchivedUserCounselorNoteRepository extends JpaRepository<ArchivedUserCounselorNote, Long> {

    // brings notes for specific client
    List<ArchivedUserCounselorNote> findByArchivedPatientId(Long archivedPatientId);

    // brings notes by note owner email address or id
    List<ArchivedUserCounselorNote> findByNoteOwnerEmailOrNoteOwnerId(String email, Long noteOwnerId);

    // Paginated query to get archived notes for a patient
    Page<ArchivedUserCounselorNote> findByArchivedPatientId(Long archivedPatientId, Pageable pageable);

    // deletes notes by note owner id or email
    void deleteByNoteOwnerEmailOrNoteOwnerId(String email, Long noteOwnerId);

    // filters archived notes by note type
    List<ArchivedUserCounselorNote> findByNoteType(NoteType noteType);

    // filters archived notes by visibility to patient
    List<ArchivedUserCounselorNote> findByIsVisibleToPatientTrue();

    // filters archived notes that are soft deleted
    List<ArchivedUserCounselorNote> findByDeletedAtIsNotNull();

    // filters archived notes created after a specific date
    List<ArchivedUserCounselorNote> findByCreatedAtAfter(LocalDateTime date);

    // filters archived notes by both type and visibility
    List<ArchivedUserCounselorNote> findByNoteTypeAndIsVisibleToPatient(NoteType noteType, Boolean isVisibleToPatient);

    List<ArchivedUserCounselorNote> findByArchivedCounselorEmail(String email);

    List<ArchivedUserCounselorNote> findByArchivedCounselorCode(String code);

    List<ArchivedUserCounselorNote> findByArchivedCounselorSpecialization(String specialization);

}
