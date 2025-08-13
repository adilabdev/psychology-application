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
- Prepared for REST/GraphQL API layer

---

## 💡 Key Features

- `addPatient()` → Checks for duplicates, restores archived or soft-deleted patients
- `softDeletePatient()` → Archives the patient and associated notes, then deletes the original record
- `updatePatient()` → Updates fields, avoids phone/email collisions, attaches new notes
- `generateUniquePatientCode()` → Generates sequential patient code with retry mechanism

---

## 🧠 Exception Handling

Custom global exception handler via `@ControllerAdvice`:

- `IllegalArgumentException` → For business rule violations  
- `MethodArgumentNotValidException` → For validation failures on `@Valid` annotated request bodies

---

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
