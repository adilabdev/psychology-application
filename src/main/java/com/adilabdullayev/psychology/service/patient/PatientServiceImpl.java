package com.adilabdullayev.psychology.service.patient;

import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import com.adilabdullayev.psychology.model.enums.AuditActionType;
import com.adilabdullayev.psychology.model.enums.NoteOwnerType;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.archived.ArchivedUserCounselorNote;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.archived.ArchivedPatients;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.archived.ArchivedPatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import com.adilabdullayev.psychology.service.notes.UserCounselorNoteService;
import com.adilabdullayev.psychology.service.audit.AuditLogService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ArchivedPatientRepository archivedPatientRepository;
    private final UserCounselorNoteRepository noteRepository;
    private final ArchivedUserCounselorNoteRepository archivedNoteRepository;
    private final CounselorService counselorService;
    private final UserCounselorNoteService noteService;
    private final AuditLogService auditLogService;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,
                              ArchivedPatientRepository archivedPatientRepository,
                              UserCounselorNoteRepository noteRepository,
                              ArchivedUserCounselorNoteRepository archivedNoteRepository,  // burası eklenmeli
                              CounselorService counselorService,
                              UserCounselorNoteService noteService,
                              AuditLogService auditLogService) {
        this.patientRepository = patientRepository;
        this.archivedPatientRepository = archivedPatientRepository;
        this.noteRepository = noteRepository;
        this.archivedNoteRepository = archivedNoteRepository; // buraya ata
        this.counselorService = counselorService;
        this.noteService = noteService;
        this.auditLogService = auditLogService;
    }

    // returns all active patients with pagination
    @Override
    public Page<Patient> getActivePatients(Pageable pageable) {
        return patientRepository.findAllByDeletedFalse(pageable);
    }

    // returns all patients including deleted ones
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll(); // TODO: Consider excluding soft-deleted patients
    }

    // finds a patient by id
    @Override
    public Patient findById(Long id) {
        return patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // filters patients using criteria from request
    @Override
    public List<Patient> filterPatients(PatientFilterRequest filterRequest) {
        return patientRepository.filterPatients(filterRequest);
    }

    // fetch notes of a patient
    @Override
    public List<UserCounselorNote> fetchNotesByPatientId(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    // search patients by keyword in email or first name
    @Override
    public List<Patient> searchPatients(String keyword) {
        return patientRepository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(keyword, keyword);
    }

    // paged search
    @Override
    public Page<Patient> searchPatients(String keyword, Pageable pageable) {
        return patientRepository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(keyword, keyword, pageable);
    }

    // get a patient by id and fetch all notes
    @Override
    public Patient getPatientById(Long patientId) {
        Patient patient = patientRepository.findByIdAndDeletedFalse(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        List<UserCounselorNote> notes = noteService.getAllNotes(patientId);
        patient.setNotes(notes);
        return patient;
    }

    // find by email
    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    // find by phone
    @Override
    public Optional<Patient> findByPhone(String phone) {
        return patientRepository.findByPhone(phone);
    }

    // find by email or phone
    @Override
    public Optional<Patient> findByEmailOrPhone(String email, String phone) {
        return patientRepository.findByEmailOrPhoneAndDeletedFalse(email, phone);
    }

    // find by email & firstName with pagination
    @Override
    public Page<Patient> findByEmailContainingIgnoreCaseAndFirstNameContainingIgnoreCase(String email, String firstName, Pageable pageable) {
        return patientRepository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(email, firstName, pageable);
    }

    @Override
    public Page<Patient> findAllByDeletedFalse(Pageable pageable) {
        return patientRepository.findAllByDeletedFalse(pageable);
    }

    // ----------------------
    // Helper methods
    // ----------------------

    // get next patient sequence number for patientCode
    private int getNextSequenceForPrefix(String prefix) {
        Integer max = patientRepository.findMaxSequenceByPrefix(prefix);
        return (max == null) ? 1 : max + 1;
    }

    // copy patient data from source to target
    private void applyNewPatientData(Patient source, Patient target) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setBirthDate(source.getBirthDate());
        target.setGender(source.getGender());
        target.setPhone(source.getPhone());
        target.setEmail(source.getEmail());
    }

    // restore archived notes for patient
    private void restoreArchivedNotes(String email, Long patientId, Patient restored) {
        List<ArchivedUserCounselorNote> archivedNotes =
                archivedNoteRepository.findByNoteOwnerEmailOrNoteOwnerId(email, patientId);

        for (ArchivedUserCounselorNote an : archivedNotes) {
            UserCounselorNote note = new UserCounselorNote();
            note.setPatient(restored);
            note.setContent(an.getContent());
            note.setNoteOwnerType(an.getNoteOwnerType());
            note.setNoteOwnerId(an.getNoteOwnerId());
            note.setNoteOwnerEmail(an.getNoteOwnerEmail());
            note.setCreatedAt(an.getCreatedAt());
            noteRepository.save(note);
        }

        // delete archived notes after restoring
        archivedNoteRepository.deleteByNoteOwnerEmailOrNoteOwnerId(email, patientId);
    }

    // ----------------------
    // Main CRUD methods
    // ----------------------

    @Override
    @Transactional
    public Patient addPatient(Patient newPatient) {
        String email = newPatient.getEmail();
        String phone = newPatient.getPhone();
        Long counselorId = getCounselorIdFromSession();
        String counselorEmail = getCounselorEmailFromSession();

        // check if patient already exists
        Optional<Patient> existingOpt = patientRepository.findByEmailOrPhone(email, phone);
        if (existingOpt.isPresent()) {
            Patient existing = existingOpt.get();
            if (!existing.getDeleted()) {
                throw new IllegalArgumentException("Bu e-posta veya telefon zaten kayıtlı.");
            }

            // delete soft-deleted patient to restore
            patientRepository.delete(existing);

            Patient restored = new Patient();
            BeanUtils.copyProperties(existing, restored, "id", "deleted", "status", "patientCode");
            restored.setDeleted(false);
            restored.setStatus(PatientStatus.NEW);
            restored.setPatientCode("PAT-" + String.format("%04d", getNextSequenceForPrefix("PAT")));
            restored.setCreatedAt(LocalDateTime.now());

            applyNewPatientData(newPatient, restored);
            restoreArchivedNotes(existing.getEmail(), existing.getId(), restored);

            auditLogService.logAction(AuditActionType.RESTORE, "Patient", restored.getId(), counselorEmail, null,
                    "Soft-deleted hasta geri yüklendi.");

            return patientRepository.save(restored);
        }

        // check if patient exists in archived table
        Optional<ArchivedPatients> archivedOpt = archivedPatientRepository.findByEmailOrPhone(email, phone);
        if (archivedOpt.isPresent()) {
            ArchivedPatients archived = archivedOpt.get();

            archivedNoteRepository.deleteByNoteOwnerEmailOrNoteOwnerId(archived.getEmail(), archived.getId());
            archivedPatientRepository.delete(archived);

            Patient restored = new Patient();
            BeanUtils.copyProperties(archived, restored, "id", "deleted", "status", "patientCode");
            restored.setDeleted(false);
            restored.setStatus(PatientStatus.NEW);
            restored.setPatientCode("PAT-" + String.format("%04d", getNextSequenceForPrefix("PAT")));
            restored.setCreatedAt(LocalDateTime.now());

            applyNewPatientData(newPatient, restored);
            restoreArchivedNotes(archived.getEmail(), archived.getId(), restored);

            auditLogService.logAction(AuditActionType.RESTORE, "Patient", restored.getId(), counselorEmail, null,
                    "Archived hasta geri yüklendi.");

            return patientRepository.save(restored);
        }

        // create a new patient
        newPatient.setPatientCode("PAT-" + String.format("%04d", getNextSequenceForPrefix("PAT")));
        newPatient.setDeleted(false);
        newPatient.setStatus(PatientStatus.NEW);
        newPatient.setCreatedAt(LocalDateTime.now());

        patientRepository.save(newPatient);

        // save notes if exist
        if (newPatient.getNotes() != null) {
            newPatient.getNotes().forEach(note -> {
                note.setPatient(newPatient);
                if (note.getNoteOwnerType() == NoteOwnerType.COUNSELOR) {
                    note.setNoteOwnerId(counselorId);
                    note.setNoteOwnerEmail(counselorEmail);
                } else {
                    note.setNoteOwnerId(newPatient.getId());
                    note.setNoteOwnerEmail(newPatient.getEmail());
                }
                noteRepository.save(note);
            });
        }

        auditLogService.logAction(AuditActionType.CREATE, "Patient", newPatient.getId(), counselorEmail, null,
                "Yeni hasta oluşturuldu.");

        return newPatient;
    }

    @Override
    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existing = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı veya silinmiş."));

        Optional<Patient> conflict = patientRepository.findByEmailOrPhone(updatedPatient.getEmail(), updatedPatient.getPhone());
        if (conflict.isPresent() && !conflict.get().getId().equals(id)) {
            throw new RuntimeException("Bu e-posta veya telefon başka bir hastaya ait.");
        }

        existing.setFirstName(updatedPatient.getFirstName());
        existing.setLastName(updatedPatient.getLastName());
        existing.setBirthDate(updatedPatient.getBirthDate());
        existing.setGender(updatedPatient.getGender());
        existing.setPhone(updatedPatient.getPhone());
        existing.setEmail(updatedPatient.getEmail());
        existing.setStatus(updatedPatient.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        Long counselorId = getCounselorIdFromSession();
        String counselorEmail = getCounselorEmailFromSession();

        if (updatedPatient.getNotes() != null) {
            updatedPatient.getNotes().forEach(note -> {
                note.setPatient(existing);
                if (note.getNoteOwnerType() == NoteOwnerType.COUNSELOR) {
                    note.setNoteOwnerId(counselorId);
                    note.setNoteOwnerEmail(counselorEmail);
                } else {
                    note.setNoteOwnerId(existing.getId());
                    note.setNoteOwnerEmail(existing.getEmail());
                }
                noteRepository.save(note);
            });
        }

        auditLogService.logAction(AuditActionType.UPDATE, "Patient", existing.getId(), counselorEmail, null, "Hasta bilgileri güncellendi.");

        return patientRepository.save(existing);
    }

    @Override
    @Transactional
    public void softDeletePatient(Long id, String reason, String deletedBy, String ipAddress) {
        Patient patient = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı veya zaten silinmiş."));

        ArchivedPatients archived = new ArchivedPatients();
        BeanUtils.copyProperties(patient, archived, "id");
        archived.setCreatedAt(patient.getCreatedAt());
        archived.setUpdatedAt(patient.getUpdatedAt());
        archived.setDeletedAt(LocalDateTime.now());
        archived.setDeletedBy(deletedBy);
        archived.setIpAddress(ipAddress);
        archived.setDeletionReason(reason);

        archivedPatientRepository.save(archived);

        if (patient.getNotes() != null && !patient.getNotes().isEmpty()) {
            List<ArchivedUserCounselorNote> archivedNotes = patient.getNotes().stream().map(note -> {
                ArchivedUserCounselorNote an = new ArchivedUserCounselorNote();
                an.setArchivedPatient(archived);
                an.setContent(note.getContent());
                an.setCreatedAt(note.getCreatedAt());
                an.setNoteOwnerType(note.getNoteOwnerType());
                an.setNoteOwnerId(note.getNoteOwnerId());
                an.setNoteOwnerEmail(note.getNoteOwnerEmail());
                an.setDeletedAt(LocalDateTime.now());
                return an;
            }).toList();

            archivedNoteRepository.saveAll(archivedNotes);
            noteRepository.deleteAll(patient.getNotes());
        }

        patientRepository.delete(patient);

        auditLogService.logAction(AuditActionType.ARCHIVE, "Patient", id, deletedBy, ipAddress, "Hasta arşivlendi. Sebep: " + reason);
    }

    // ----------------------
    // Session helpers
    // ----------------------

    private Long getCounselorIdFromSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        return Long.parseLong(auth.getName()); // TODO: Replace with CustomUserDetails if available
    }

    private String getCounselorEmailFromSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        return user.getUsername(); // TODO: Replace with CustomUserDetails if available
    }
}
