package com.adilabdullayev.psychology.service;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import com.adilabdullayev.psychology.model.patient.ArchivedPatients;
import com.adilabdullayev.psychology.repository.patient.ArchivedPatientRepository;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.model.notes.ArchivedUserCounselorNote;
import com.adilabdullayev.psychology.model.notes.NoteOwnerType;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;


@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    private final UserCounselorNoteRepository noteRepository;

    private final ArchivedUserCounselorNoteRepository archivedUserCounselorNoteRepository;

    private final ArchivedPatientRepository archivedPatientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository,
                          ArchivedPatientRepository archivedPatientRepository,
                          UserCounselorNoteRepository noteRepository,
                          ArchivedUserCounselorNoteRepository archivedUserCounselorNoteRepository) {
        this.patientRepository = patientRepository;
        this.archivedPatientRepository = archivedPatientRepository;
        this.noteRepository = noteRepository;
        this.archivedUserCounselorNoteRepository = archivedUserCounselorNoteRepository;
    }

    // brings all active patients
    public Page<Patient> getActivePatients(Pageable pageable) {
        return patientRepository.findByDeletedFalse(pageable);
    }

    // brings all patients soft deleted or active
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // brings active patient by id
    public Patient findById(Long id) {
        return patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // brings a list by parameters of filter request
    public List<Patient> filterPatients(PatientFilterRequest filterRequest) {
        return patientRepository.filterPatients(filterRequest);
    }

    // brings notes by patient id
    public List<UserCounselorNote> fetchNotesByPatientId(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    /*
    * addpatient method
    * - checks by is email and phone
    * -- if there is active client throws error
    * -- if there is deleted client restores and updates
    * - products patient code
    * - associate patient notes
    * */
    @Transactional
    public Patient addPatient(Patient newPatient) {
        logger.info("addPatient çağrıldı. Email: {}, Telefon: {}", newPatient.getEmail(), newPatient.getPhone());

        // checking if there is a patient with same email address or phone number
        Optional<Patient> existingOpt = patientRepository.findByEmailOrPhone(newPatient.getEmail(), newPatient.getPhone());

        if (existingOpt.isPresent()) {
            Patient existing = existingOpt.get();
            if (!existing.getDeleted()) {
                logger.error("Bu e-posta veya telefon zaten kayıtlı. Email: {}, Telefon: {}", newPatient.getEmail(), newPatient.getPhone());
                throw new IllegalArgumentException("Bu e-posta veya telefon zaten kayıtlı.");
            }

            // restore if there is soft deleted patient and update the informations
            logger.info("Silinmiş hasta bulundu, geri getiriliyor. ID: {}", existing.getId());

            patientRepository.delete(existing);

            Patient restored = new Patient();
            BeanUtils.copyProperties(existing, restored, "id", "deleted", "status", "patientCode");
            restored.setDeleted(false);
            restored.setStatus(PatientStatus.YENI);

            String code = generateUniquePatientCode();
            restored.setPatientCode(code);

            applyNewPatientData(newPatient, restored);

            restoreArchivedNotes(existing.getEmail(), existing.getId(), restored);

            logger.info("Silinmiş hasta geri yüklendi. Yeni Kod: {}", code);
            return patientRepository.save(restored);
        }

        // is there any deleted = true
        Optional<ArchivedPatients> archivedOpt = archivedPatientRepository.findByEmailOrPhone(newPatient.getEmail(), newPatient.getPhone());

        if (archivedOpt.isPresent()) {
            ArchivedPatients archived = archivedOpt.get();


            // delete patient and it's notes from archive
            archivedUserCounselorNoteRepository.deleteByNoteOwnerEmailOrNoteOwnerId(
                    null, // not email
                    null  // not Id
            );
            archivedPatientRepository.delete(archived);

            Patient restored = new Patient();
            BeanUtils.copyProperties(archived, restored, "id", "deleted", "status", "patientCode");
            restored.setDeleted(false);
            restored.setStatus(PatientStatus.YENI);

            String code = generateUniquePatientCode();
            restored.setPatientCode(code);

            applyNewPatientData(newPatient, restored);
            restoreArchivedNotes(null, null, restored); // not email and not id

            return patientRepository.save(restored);
        }

        // create a new patient
        String code = generateUniquePatientCode();
        newPatient.setPatientCode(code);
        newPatient.setDeleted(false);
        newPatient.setStatus(newPatient.getStatus() != null ? newPatient.getStatus() : PatientStatus.YENI);

        if (newPatient.getNotes() != null) {
            newPatient.getNotes().forEach(n -> n.setPatient(newPatient));
        }

        logger.info("Yeni hasta oluşturuldu. Kod: {}", code);
        return patientRepository.save(newPatient);
    }

    // associate notes with new patient
    private void restoreArchivedNotes(String email, Long ownerId, Patient restored) {
        List<ArchivedUserCounselorNote> archivedNotes = archivedUserCounselorNoteRepository.findByNoteOwnerEmailOrNoteOwnerId(email, ownerId);
        for (ArchivedUserCounselorNote an : archivedNotes) {
            UserCounselorNote newNote = new UserCounselorNote();
            BeanUtils.copyProperties(an, newNote, "id", "patient");
            newNote.setPatient(restored);
            restored.getNotes().add(newNote);
        }
        logger.info("Arşivlenmiş notlar geri yüklendi. Sayı: {}", archivedNotes.size());
    }

    // apply the new patient informations on current patient
    private void applyNewPatientData(Patient src, Patient dest) {
        dest.setFirstName(src.getFirstName());
        dest.setLastName(src.getLastName());
        dest.setBirthDate(src.getBirthDate());
        dest.setGender(src.getGender());
        dest.setPhone(src.getPhone());
        dest.setEmail(src.getEmail());
        dest.setStatus(src.getStatus() != null ? src.getStatus() : PatientStatus.YENI);

        if (src.getNotes() != null) {
            src.getNotes().forEach(note -> {
                note.setPatient(dest);
                dest.getNotes().add(note);
            });
        }
    }


    // updates patient informations, email address and phone number must be unique
    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient existingPatient = patientRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> {
                    logger.warn("Danışan bulunamadı veya silinmiş. ID: {}", id);
                    return new RuntimeException("Danışan bulunamadı veya silinmiş.");
                });

        // is email or phone using by another client
        Optional<Patient> conflictOpt = patientRepository
                .findByEmailOrPhone(updatedPatient.getEmail(), updatedPatient.getPhone());

        if (conflictOpt.isPresent() && !conflictOpt.get().getId().equals(id)) {
            throw new RuntimeException("Bu e-posta veya telefon başka bir danışana ait.");
        }

        // update fields
        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setBirthDate(updatedPatient.getBirthDate());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setPhone(updatedPatient.getPhone());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setStatus(updatedPatient.getStatus());

        // add new notes
        if (updatedPatient.getNotes() != null) {
            for (UserCounselorNote note : updatedPatient.getNotes()) {
                note.setPatient(existingPatient);
                note.setNoteOwnerType(NoteOwnerType.PATIENT);
                note.setNoteOwnerId(existingPatient.getId());
                note.setNoteOwnerEmail(existingPatient.getEmail());

                existingPatient.getNotes().add(note);
            }
        }

        return patientRepository.save(existingPatient);
    }


    /*
    * deletes patient by soft delete
    * - patient is getting archived
    * - notes is getting archived
    * - pyshically deletes patient from patients table
    */
    @Transactional
    public void softDeletePatient(Long id, String deletionReason, String deletedBy, String ipAddress) {
        logger.info("Soft delete işlemi başlatıldı. Hasta ID: {}", id);

        Patient patient = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    logger.warn("Silinmek istenen hasta bulunamadı veya zaten silinmiş. ID: {}", id);
                    return new RuntimeException("Hasta bulunamadı veya zaten silinmiş.");
                });

        ArchivedPatients archived = new ArchivedPatients();
        BeanUtils.copyProperties(patient, archived, "id");

        ArchivedPatients savedArchived = archivedPatientRepository.save(archived);
        logger.info("Hasta arşivlendi. Arşiv ID: {}", savedArchived.getId());

        if (patient.getNotes() != null && !patient.getNotes().isEmpty()) {
            List<ArchivedUserCounselorNote> archivedNotes = patient.getNotes().stream().map(note -> {
                ArchivedUserCounselorNote an = new ArchivedUserCounselorNote();
                an.setArchivedPatient(savedArchived);
                an.setContent(note.getContent());
                an.setCreatedAt(note.getCreatedAt());
                an.setNoteOwnerType(note.getNoteOwnerType());
                an.setNoteOwnerId(note.getNoteOwnerId());
                an.setNoteOwnerEmail(note.getNoteOwnerEmail());
                return an;
            }).toList();

            archivedUserCounselorNoteRepository.saveAll(archivedNotes);
            logger.info("Notlar arşivlendi. Not sayısı: {}", archivedNotes.size());

            patient.getNotes().forEach(note -> note.setPatient(null));
            patient.getNotes().clear();
        }

        patientRepository.delete(patient);
        logger.info("Hasta patients tablosundan silindi. ID: {}", id);
    }

    // generating patient code
    /*
    * prefix and ordered numbers
    * if there is note unique code throw an error
    * */
    private String generateUniquePatientCode() {
        String prefix = "PAT";
        final int maxAttempts = 5;

        for (int i = 0; i < maxAttempts; i++) {
            Integer maxSeq = patientRepository.findMaxSequenceByPrefix(prefix);
            int nextSeq = (maxSeq == null) ? 1 : maxSeq + 1;
            String code = prefix + "-" + String.format("%04d", nextSeq);

            if (!patientRepository.existsByPatientCode(code)) {
                return code;
            }
        }
        throw new RuntimeException("Hasta kodu üretilemedi, benzersiz bir kod oluşturulamadı.");
    }

}
