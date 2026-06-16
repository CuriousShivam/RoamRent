# 🚗 Roam Rent

Roam Rent is a modern, three-way Car Rental Application built using a **Java Servlet REST API** backend, a **Next.js** frontend, and a remote **PostgreSQL** cloud database. The platform connects customers looking for travel rides with independent car owners/drivers who can list their vehicles and bid on trip requests.

---

## 👥 Project Stakeholders

1. **Customers**: Users who need to book a car for single or return trips.
2. **Car Owners / Drivers**: Users who list their personal vehicles and make custom pricing offers to customers.

---

## 📋 Functional Requirements

### 👤 1. For Customers
*   **Registration & Login**: Secure user authentication to create a personal account.
*   **Raise Trip Requests**: Submit travel needs by inputting the following data:
    *   Pickup Location & Destination (City/State)
    *   Travel Date and Time
    *   Passenger Count
    *   Trip Type Selection (Is a return trip required or not?)
*   **Offer Notification**: View real-time pricing offers (Rent for trip) sent by local drivers.
*   **Trip Confirmation**: Select and confirm the best offer from the list of bidding drivers.
*   **View Confirmed Trips**: Access a personal dashboard showing all booked and finalized travels.

### 🪪 2. For Drivers
*   **Vehicle Registration**: Register a profile along with detailed vehicle specifications:
    *   Vehicle Manufacturer/Company
    *   License Plate Number
    *   Total Seating Capacity
*   **View Matched Requests**: Access a dynamic dashboard showing active trip requests posted by local customers.
*   **Submit Pricing Bids**: Offer custom travel costs (Rent for trip) for open customer requests.
*   **View Confirmed Trips**: Keep track of trips won and confirmed by customers.

---

## 🧠 Core Business Logic: Trip Matching

To keep the platform optimized, trip requests are automatically routed using server-side SQL matching rules. A driver will **only** see an active customer request if:
1.  The driver's vehicle has a **seating capacity greater than or equal to** the customer's passenger count.
2.  The driver operates in the **same City or same State** as the customer's requested pickup location.

---

## 🏗️ Project Architecture & Tech Stack

The project is structured as a decoupled **Monorepository** separating the UI presentation from the data access layers.

*   **Frontend**: Next.js (React, Tailwind CSS)
*   **Backend Engine**: Java Servlets (Jakarta EE 10)
*   **Database**: Remote PostgreSQL Instance
*   **Libraries**: JDBC (Data Access), GSON (JSON serialization)

### 📁 Directory Layout

```text
roam-rent/
├── roam-rent-frontend/       # Next.js UI Application
│   ├── src/app/              # Dashboard pages (Customer & Driver)
│   └── package.json          # Node dependencies
│
└── roam-rent-backend/        # IntelliJ Maven Java Project
    ├── pom.xml               # Maven configuration
    └── src/main/java/com/roamrent/
        ├── config/           # PostgreSQL DB connections
        ├── model/            # Plain Java Objects (User, Vehicle, Trip)
        ├── dao/              # Raw JDBC SQL Queries & Logic
        └── servlet/          # Pure REST APIs returning JSON text
```

---

## 🚀 Local Development Setup

### Backend (Java)
1. Open `roam-rent-backend` inside IntelliJ IDEA.
2. Configure your remote PostgreSQL credentials inside `MyDatabase.java`.
3. Start the application using your configured local Tomcat engine or **Smart Tomcat** plugin. The API will run locally at `http://localhost:8080/`.

### Frontend (Next.js)
1. Open a terminal inside the `roam-rent-frontend` folder.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development environment interface:
   ```bash
   npm run dev
   ```
4. Open `http://localhost:3000` in your web browser.

