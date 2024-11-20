# Hotel Reservation System

An enterprise-level hotel reservation system built using Jakarta Enterprise Edition (JEE), designed for hotel staff, guests, and third-party providers.

For detailed Project Specifications and Use Case descriptions: [Project Specifications.pdf](https://github.com/TimothyLawSongEn/HotelReservationSystem/blob/main/Project%20Specifications.pdf)

---

## Technologies Used

- **Jakarta EE Components:** EJB (stateless and singleton session beans), JPA, JAX-WS
- **Database:** MySQL with EclipseLink JPA for ORM
- **Application Server:** GlassFish
- **Client Communication:**
  - RMI (Remote Method Invocation) for Enterprise Application Clients
  - SOAP APIs using JAX-WS for third-party integration
- **Additional Features:**
  - Bean Validation (JSR-380) ensures all data (e.g., guest details) meets business requirements
  - Scheduled Methods for automated tasks (e.g. automated room allocation at 2am daily)
 
---

## Overall Architecture

![image](https://github.com/user-attachments/assets/8cec74e4-8a90-4b6e-b956-d6bdc324b826)

---

## Backend Architecture

The backend consists of a single EJB module with these primary functions implemented using session beans:

### 1. Personnel Management
- **CRUD Operations:**
  - Manage `Guest`, `Staff`, and `Third-Party Providers`.
  - Role-based differentiation for staff ensures only authorized personnel can perform sensitive operations.

### 2. Room Management
- **CRUD Operations:**
  - Manage `Room`, `RoomType`, and `RoomRates`.
  - RoomTypes support different rates: `Normal`, `Published`, `Peak`, and `Promo`.
- **Dynamic Room Rates:**
  - Rates can vary based on the time period and promotion types.

### 3. Room Availability & Booking Processing
- **Room Availability Bean:**
  - Centralized singleton session bean to track room availability across room types for efficient lookups.
  - Ensures consistency and prevents overbooking through container-managed transactions.
- **Booking Bean:**
  - Stateless session bean handles booking creation and validation.
  - Supports business rules like non-overlapping bookings and required guest details.

---

## Frontend Clients [Command-Line]

1. **Enterprise Application Clients (via RMI):**
   - **Management Client:**  
     - For hotel staff. Allows room and personnel management, and booking processing.  
   - **Reservation Client:**  
     - For guests. Provides room search and booking functionality.
2. **Web Service Client (via SOAP):**
   - Designed for third-party providers to query room availability and book rooms.
   - Built using the JAX-WS library for SOAP communication.

---

## Web Services

- **SOAP Endpoints:**
  - Room searching and booking exposed via JAX-WS.
  - JAXB handles XML serialization/deserialization.
- **Language-Neutral Integration:**
  - Third-party clients can integrate regardless of their programming language or platform.

---


## **Deployment Steps**

1. **Prerequisites**:  
   - NetBeans IDE with GlassFish configured as the server.  
   - MySQL installed and connected to GlassFish.  
   - Database named `HotelReservationSystem` created with appropriate user privileges.  
   - Add the MySQL JDBC driver to GlassFish.
   - Set up a JDBC connection pool and resource in GlassFish Admin Console.

3. **Clean and Build the Project**:  
   - In NetBeans, right-click the project and select **Clean and Build**.  

4. **Deploy to GlassFish**:  
   - Deploy the EAR/WAR files to GlassFish.  
   - JPA auto-generates the necessary database tables.

5. **Run Clients (EAC only)**:  
   - Locate the **EAC module** in the **Projects** view of NetBeans.
   - Right-click the module and select **Run** to launch the console interface.
   - The client communicates with the backend via dependency-injected session beans.

---

## Design Considerations

- **Transactional Integrity:**
  - Container-managed transactions ensure atomic operations (e.g., no double bookings).
- **Concurrency Management:**
  - Singleton session bean tracks room availability and handles concurrent requests safely.
- **Extensibility:**
  - Modular design allows easy addition of new features like loyalty programs or additional room types.
- **Scalability:**
  - Stateless session beans and SOAP APIs allow the backend to scale with increasing demand.

---

## Future Enhancements
- **REST API Support:**
  - Introduce RESTful endpoints to complement SOAP for modern integrations.
- **Web-Based Frontend:**
  - Develop a web application using React or Angular for better user experience.
- **Real-Time Updates:**
  - Use WebSockets to provide real-time notifications for booking confirmations or availability changes.
