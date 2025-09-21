# SpareWay – Online Vehicle Spare Parts Marketplace (AAD Final Project IJSE)

SpareWay is a web-based platform designed to connect buyers with sellers of vehicle spare parts.
It allows buyers to search, browse, and purchase spare parts, while sellers can manage ads efficiently.
Admins oversee user management and ensure secure operations.

This project demonstrates **Spring Boot (Backend)** and **HTML/CSS/Bootstrap/JavaScript (Frontend)** integration with **JWT authentication, role-based access, and secure APIs.**

---------

## Project Purpose
The primary goals of **SpareWay** are:

-Provide a platform for buyers to search and purchase vehicle spare parts.

-Enable sellers to post, edit, and manage spare part listings.

-Ensure secure login & registration with JWT authentication.

-Offer dashboards tailored to buyers, sellers, and admins.

-Showcase practical use of Spring Boot, REST APIs, Spring Security, JWT, MySQL, and email notifications.

-----------

## Screenshots

### Home Page
![Home Page](Front_End/img/Screenshot%202025-09-21%20at%2021.22.17.png)

### User DashBoard
![User DashBoard](Front_End/img/Screenshot%202025-09-21%20at%2021.23.32.png)

### Admin DashBoard
![Admin DashBoard](Front_End/img/Screenshot%202025-09-21%20at%2021.25.00.png)

-----------

## Setup Instructions

### 1️⃣ Clone the Repository
git clone https://github.com/Hirusha-silva/Second_Semester_Final_Project.git

### 2️⃣ Backend Setup (Spring Boot)

Configure the application.properties file with your local database:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/SpareWay?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
jwt.secret=YOUR_SECRET_KEY
jwt.expiration=3600000
jwt.refresh.expiration=86400000
```

Run the application:mvn spring-boot:run

The backend will start on http://localhost:8080

### 3️⃣ DB Setup

Add admin manually to the database.
INSERT INTO users (user_id,address,email,name,password,phone,role,username) VALUES
(1 , 'Wadduwa' , 'hirushasilva222@gmail.com' , 'Hirusha' , '$2a$10$YfwA628jRPPOinhFvxrmn.VEqEHPVfUl/YmeD64AKdFpOch59yQfe' , '0721542054' , 'ADMIN' , 'hirusha');


Add Categories manually to the database.
INSERT INTO categories (category_id,name) VALUES (1 , 'Parts');

Add Vehicle models manually to the database.
INSERT INTO vehicle_model (model_id,brand,model) VALUES (1 , 'TOYOTA' , 'Prius');



### 4️⃣ Frontend Setup (HTML/CSS/Bootstrap + jQuery)

Since the frontend is static HTML/CSS/JS:Simply open index.html in a browser, or Serve it via a lightweight server (e.g., Live Server in VS Code).The frontend will connect to the backend API running at http://localhost:8080

##  Demo Video

📌 Watch the Demo on YouTube : (Upload your demo video and replace the link above. Use a title like: "SpareWay – AAD Final Project (IJSE)")

##  Tech Stack

- Backend: Spring Boot, Spring Security, JWT, Hibernate, MySQL
- Frontend: HTML, CSS, Bootstrap, jQuery
- Build Tools: Maven
- Version Control: GitHub

##  Author
- Developed by ***Hirusha Silva*** as part of the IJSE GDSE – Advanced API Development final project.

 
