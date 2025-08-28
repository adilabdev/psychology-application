# Patient Management System (Spring Boot Microservice)

This is a backend service for managing patients in a psychological counseling application. The system demonstrates advanced use of Spring Boot with a strong emphasis on modularity, data integrity, and extensibility.

## 🎯 Purpose

The application manages psychological patient records and associated notes. It includes:

- Patient registration and update with validation
- Soft deletion with archiving and recovery
- Unique patient code generation logic
- Note association with owner metadata
- Transactional and consistent data handling

> Designed to serve as the foundation for a full-featured clinical psychology management system.

---

## 🛠 Technologies Used

- Java 17+
- Spring Boot 3
- Spring Data JPA
- PostgreSQL / H2 (for development)
- Maven
- SLF4J Logging
- BeanUtils
- Spring Validation (`@Valid`)
- Spring Exception Handling (`@ControllerAdvice`)

---

## 🧱 Architecture

```
Controller Layer (future plans)
│
├── Service Layer → Handles business logic, transactions
│   └── add, update, soft-delete, restore logic, etc.
│
└── Repository Layer → JPA-based data access
```

- Follows SOLID principles
- Separates concerns clearly across layers
- Modular and maintainable  
- Ready for RESTful API expansion 

---

## 💡 Key Features

- Add, update, archive (soft delete) patients and counselors 
- Add, filter, archive counselors  
- Store notes linked to patients and counselors  
- Enum-based modeling for roles, gender, status  
- Pagination and search support

---

## 🧠 Exception Handling

Custom global exception handler via `@ControllerAdvice`:

- `IllegalArgumentException` → For business rule violations  
- `MethodArgumentNotValidException` → For validation failures on `@Valid` annotated request bodies

---

## REST API Endpoints

## 🧍 Patient API Endpoints (`/patients`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/patients/active` | Retrieve active patients (paginated) |
| `GET`  | `/patients/all` | Retrieve all patients (active + archived) |
| `GET`  | `/patients/ping` | Health check for patient service |
| `GET`  | `/patients/{id}` | Get patient by ID |
| `POST` | `/patients` | Create a new patient |
| `PUT`  | `/patients/{id}` | Update patient details |
| `DELETE` | `/patients/{id}` | Soft-delete (archive) a patient |
| `POST` | `/patients/filter` | Filter patients by criteria |
| `GET`  | `/patients/paged` | Get paginated list of active patients |

---

## 🧑‍⚕️ Counselor API Endpoints (`/counselors`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/counselors` | Retrieve all counselors |
| `GET`  | `/counselors/all` | Retrieve all visible counselors |
| `GET`  | `/counselors/active` | Retrieve active counselors |
| `GET`  | `/counselors/paged` | Get paginated list of active counselors |
| `GET`  | `/counselors/search?query=abc` | Search counselors by keyword |
| `GET`  | `/counselors/{id}/sessions` | Get session data for a specific counselor |
| `GET`  | `/counselors/stats` | Get counselor statistics |
| `POST` | `/counselors` | Create a new counselor |
| `PUT`  | `/counselors/{id}` | Update counselor details |
| `DELETE` | `/counselors/{id}` | Soft-delete (archive) a counselor |
| `POST` | `/counselors/filter` | Filter counselors by criteria |


---

### 🔹 Active Notes (`/patients/{patientId}/notes`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/patients/{patientId}/notes` | Create a new note for a specific patient |
| `GET`  | `/patients/{patientId}/notes` | Retrieve all notes for a specific patient |
| `GET`  | `/patients/{patientId}/notes/type/{noteOwnerType}` | Filter notes by owner type (e.g., COUNSELOR, SYSTEM) |
| `GET`  | `/patients/{patientId}/notes/search` | Search notes by keyword (optionally filter by owner type) |
| `GET`  | `/patients/{patientId}/notes/filter/type` | Filter notes by note type (e.g., INFORMATION, WARNING) |
| `GET`  | `/patients/{patientId}/notes/visible` | Get notes marked as visible to the patient |
| `GET`  | `/patients/{patientId}/notes/deleted` | Get soft-deleted notes |
| `GET`  | `/patients/{patientId}/notes/created-after` | Get notes created after a specific date |
| `GET`  | `/patients/{patientId}/notes/filter` | Filter notes by type and visibility |

---

### 🔹 Archived Notes (`/patients/{patientId}/archived-notes`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/patients/{patientId}/archived-notes` | Create an archived note for a specific patient |
| `GET`  | `/patients/{patientId}/archived-notes` | Retrieve archived notes with pagination |
| `GET`  | `/patients/{patientId}/archived-notes/filter/type` | Filter archived notes by note type |
| `GET`  | `/patients/{patientId}/archived-notes/visible` | Get archived notes visible to the patient |
| `GET`  | `/patients/{patientId}/archived-notes/deleted` | Get soft-deleted archived notes |
| `GET`  | `/patients/{patientId}/archived-notes/created-after` | Get archived notes created after a specific date |
| `GET`  | `/patients/{patientId}/archived-notes/filter` | Filter archived notes by type and visibility |

---

> These endpoints support full lifecycle management of notes, including creation, filtering, visibility control, and soft deletion — for both active and archived patient records.



## 📁 Example JSON (Request Payload)

```json
{
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "birthDate": "1995-07-20",
  "gender": "Erkek",
  "phone": "+90597433533",
  "email": "ahaqd.ypaaz@example.com",
  "notes": [
    {
      "title": "İlk görüşme öncesi düşünceler",
      "content": "son zamanlarda kendimiiyi ve pozitif hissettiğim için görüşmemize daha da pozitif bakıyorum pozitif bakıyorum.",
      "type": "USER"
    }
  ]
}

```

---

---

## 📄 Sample Response (POST /patients)

```json
{
  "deleted": false,
  "createdAt": "2025-08-12T16:58:58.882331",
  "updatedAt": "2025-08-12T16:58:58.882331",
  "id": 9,
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "birthDate": "1995-07-20",
  "gender": "Erkek",
  "phone": "+90597433533",
  "email": "ahaqd.ypaaz@example.com",
  "notes": [
    {
      "id": 9,
      "title": "İlk görüşme öncesi düşünceler",
      "content": "son zamanlarda kendimi iyi ve pozitif hissettiğim için görüşmemize daha da pozitif bakıyorum.",
      "noteOwnerType": "SYSTEM",
      "noteOwnerId": null,
      "noteOwnerEmail": null,
      "createdAt": "2025-08-12T16:58:58.8873836"
    }
  ],
  "patientCode": "PAT-0006",
  "status": "YENI",
  "sessionCount": null,
  "lastSessionDate": null,
  "age": 30
}
```

---

## 🔮 Future Plans

This project is intended to evolve into a **complete psychology clinic backend platform**. Planned extensions include:
✅ Counselor, intern, secretary roles and entity modeling
✅ Role-based access and permissions via Spring Security
✅ Appointment/session management
✅ Payment & billing modules
✅ Advanced note tagging and versioning
✅ RESTful API layer with Swagger/OpenAPI
✅ Docker + Docker Compose setup
✅ Postman collection for easy API testing
✅ Unit tests & integration tests (Testcontainers)
✅ Patient session history tracking (chronological view of therapy)
✅ Therapist feedback / post-session comments
✅ Notification system (email/SMS reminders for appointments)
✅ Audit logs (who did what & when — accountability & compliance)
✅ Localization (multi-language support for broader clinic use)
✅ Patient portal integration (for future web/mobile use)
✅ Soft delete recovery UI/endpoint (trash bin-style restore)
---

## 🚀 Run Instructions

1. Clone the repository  
2. Configure your database settings in `application.properties`  
3. Run the application with:

```bash
mvn spring-boot:run
```

---

## 👨‍💻 Developer Notes

- Exception handling is centralized  
- Validation via `@Valid` and `@NotBlank` annotations  
- Designed to be modular and maintainable  
- JSON responses are verbose and detailed for frontend readiness  
- Unit tests will be added for business logic validation

---

## 📫 Contact

If you're a developer, recruiter, or just curious, feel free to reach out or clone and experiment.

> Built with real-world practices in mind — not just code that works, but code that lasts.
