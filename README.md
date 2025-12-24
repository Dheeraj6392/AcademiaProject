# 📚 Academia Full-Stack Project  
A full-stack web application for uploading, storing, and managing academic documents such as PDFs, notes, assignments, and question papers.  
This project is built using:

- **Angular (Frontend)**
- **Spring Boot (Backend)**
- **MySQL Database**
- **Cloudinary for File Upload**
- **REST APIs**
- **CORS + Spring Security Configuration**

---

## 🚀 Features

### 🔹 File Upload
- Upload **PDF documents** from the Angular UI.
- Automatically store file in **Cloudinary (raw upload)**.
- Backend returns secure URL + metadata.

### 🔹 Metadata Storage
Each upload stores:
- Semester  
- Year  
- Course Name  
- File URL  
- Cloudinary Public ID  

All metadata is saved in **MySQL database**.

### 🔹 APIs Provided
```
POST /api/files/upload        → Upload a file with metadata  
GET  /api/files               → List all uploaded files  
GET  /api/files/{id}          → Fetch a single file  
DELETE /api/files/{id}        → Delete file + Cloudinary asset
```

---

## 🏛 Project Structure

```
Academia_FullStack/
│
├── Backend/        # Spring Boot Application
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── config/
│   └── resources/application.properties
│
└── Frontend/       # Angular Application
    ├── src/app/
    │     ├── upload-papers/
    │     └── services/
    └── angular.json
```

---

## 🛠️ Technologies Used

### Frontend  
- Angular 17  
- TypeScript  
- Reactive Forms  
- HTML/CSS  
- Bootstrap  

### Backend  
- Spring Boot 3  
- Spring MVC  
- Spring Data JPA  
- MySQL  
- Cloudinary Java SDK  
- Lombok  

---

## ⚙️ Backend Setup Guide

### 1️⃣ Clone Repository
```bash
git clone https://github.com/Dheeraj6392/Academia_FullStack.git
cd Academia_FullStack/Backend
```

### 2️⃣ MySQL Database Setup
Run these commands:

```sql
CREATE DATABASE uploads_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'upload_user'@'localhost' IDENTIFIED BY 'System123@';
GRANT ALL PRIVILEGES ON uploads_db.* TO 'upload_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3️⃣ Configure `application.properties`
```
spring.datasource.url=jdbc:mysql://localhost:3306/uploads_db?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=upload_user
spring.datasource.password=System123@

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

cloudinary.cloud-name=dmzb1cnwn
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET
cloudinary.upload-preset=unsigned_pdf
```

### 4️⃣ Run Backend
```bash
mvn spring-boot:run
```

---

## 🌐 Frontend Setup Guide

### 1️⃣ Install Dependencies
```bash
cd Frontend
npm install
```

### 2️⃣ Start Angular App
```bash
ng serve --open
```

### 3️⃣ Update API URL in Angular service
```ts
private apiUrl = "http://localhost:8080/api/files/upload";
```

---

## 📤 Postman File Upload Example

### Method  
```
POST /api/files/upload
```

### Body → `form-data`

| Key        | Type | Value |
|------------|------|-------|
| file       | File | choose PDF |
| semester   | Text | 5 |
| year       | Text | 2024 |
| courseName | Text | Operating Systems |

### Example Response
```json
{
  "id": 1,
  "fileUrl": "https://res.cloudinary.com/.../abc.pdf",
  "publicId": "abc12xyz",
  "semester": "5",
  "year": "2024",
  "courseName": "Operating Systems"
}
```

---

## 🧪 Testing Steps

✔ Start MySQL  
✔ Start Spring Boot backend  
✔ Start Angular frontend  
✔ Open browser → http://localhost:4200  
✔ Upload a PDF  
✔ Check Cloudinary Dashboard  
✔ Check MySQL Workbench  

---

## 📦 Database Table: `uploaded_files`

| Column | Type |
|--------|------|
| id | BIGINT (PK) |
| semester | VARCHAR |
| year | VARCHAR |
| course_name | VARCHAR |
| file_url | TEXT |
| public_id | VARCHAR |
| uploaded_at | TIMESTAMP |

---

## 🤝 Contribution
Pull requests are welcome!  
Feel free to report bugs or request features.

---

## ⭐ Support
If you like this project, please give it a ⭐ on GitHub!

