# Lost & Found App — Android + Spring Boot

## Overview
The Lost & Found Application is a mobile and backend system developed using Android Studio and Spring Boot.  
It allows users to report, search, and retrieve information about lost or found items. The Android app communicates with the backend through REST APIs, and the backend manages storage and retrieval of item data.

## Android Application Features
- Report lost items  
- Report found items  
- View all reported items  
- Search items by keywords  
- Communicates with the backend using REST APIs  
- User-friendly interface  

## Spring Boot Backend Features
- REST API endpoints for adding and retrieving items  
- JSON-based request and response handling  
- Database connectivity using MySQL or H2  
- CORS enabled for Android integration  

# Project Setup

## A. Backend Setup (Spring Boot)

### Requirements
- Java 17 or compatible version  
- Maven  
- IntelliJ IDEA, Eclipse, or Spring Tool Suite  

### Steps to Run the Backend
1. Open the backend folder in your preferred IDE.  
2. Configure the database settings in `application.properties`.  
3. Run the backend using the following command:  
   ```
   mvn spring-boot:run
   ```
4. The backend will start on:  
   ```
   http://localhost:8080
   ```

### API Endpoints
```
POST /items/add       Add a lost or found item  
GET  /items/all       Retrieve all items  
GET  /items/search    Search items by keyword  
```

## B. Android App Setup

### Requirements
- Android Studio (Hedgehog or higher)  
- Android emulator or a physical device  
- Running backend server  

### Steps to Run the Android App
1. Open the Android project folder in Android Studio.  
2. Locate the API configuration file (such as ApiService.java).  
3. Update the base URL in the file:
   ```java
   public static final String BASE_URL = "http://YOUR_IP_ADDRESS:8080/";
   ```
4. Connect an Android device or use the emulator.  
5. Click the Run button in Android Studio.  

### Using the Android App
1. Launch the app.  
2. Choose either the Lost Item or Found Item option.  
3. Enter the required item details.  
4. Submit the form to store the item in the backend.  
5. Browse or search through the items list.  

# Project Structure

## Android Project
```
Android/
│── app/
│   ├── java/com/example/lostfound/
│   │    ├── MainActivity.java
│   │    ├── AddItemActivity.java
│   │    ├── ApiService.java
│   │    ├── Models/
│   │    └── Adapters/
│   └── res/
│        ├── layout/
│        ├── drawable/
│        └── values/
```

## Spring Boot Project
```
Backend/
│── src/
│   ├── main/java/com/app/lostfound/
│   │     ├── Controllers/
│   │     ├── Services/
│   │     ├── Repositories/
│   │     └── Models/
│   └── main/resources/
│         ├── application.properties
│── pom.xml
```

# GitHub Repository Link
Add your repository link here:  
`<PASTE GITHUB LINK HERE>`

# Technologies Used
- Android Studio (Java or Kotlin)  
- Spring Boot  
- REST API architecture  
- MySQL or H2 Database  
- Maven build system  

# Future Enhancements
- User authentication and login  
- Push notifications  
- Image upload support  
- Cloud-based backend system  
- Admin dashboard  

# Contributors
- Name: Shreyash Sharma  
  Section: A  
  Group Number: 9

# Submission Details
All required project files including Android source code, Spring Boot backend, and README have been compressed into:

```
section_A_group_9.zip
```
