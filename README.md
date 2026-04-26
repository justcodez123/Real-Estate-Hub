# Real Estate Hub 🏠

A comprehensive full-stack real estate platform for managing property listings, agents, and user subscriptions. This project features a robust Spring Boot backend and a modern React frontend, designed for scalability and ease of use.

## 🚀 Project Overview

Real Estate Hub allows users to browse properties, agents to manage their listings, and administrators to oversee the platform. It includes advanced features like image uploads, subscription tiers, and agent-client communication tools.

### 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot 3.5.10, Spring Data JPA, PostgreSQL, Lombok.
- **Frontend:** React 19, Axios, React Router Dom 7, Vanilla CSS.
- **Infrastructure:** AWS Deployment ready (S3, EC2/Elastic Beanstalk).

---

## ✨ Features

### ✅ Currently Implemented
- **User Authentication & Management:** Secure registration and login for users and agents.
- **Property Management:** Create, Read, Update, and Delete (CRUD) property listings.
- **Image Upload:** Integrated property image uploading (with AWS S3 support).
- **Agent Dashboard:** Dedicated space for agents to manage their properties and inquiries.
- **Subscription System:** Multi-tier subscription plans for users and agents.
- **Favorites & Shortlists:** Users can save properties they are interested in.
- **Communication:** "Contact Agent" and "Schedule Viewing" features.
- **Builder Groups:** Support for real estate builder groups and organizations.
- **Validation:** Robust phone number and form validation.

### 🏗️ In Progress / Roadmap
- **AI Integrated Chatbot:** intelligent assistant to help users find properties and answer FAQs.
- **Gamification (Point System):** Track user activity and reward engagement with point variables.
- **Proximity Search:** Find "Nearby Estates" based on user location or specific coordinates.
- **Advanced Analytics:** Dashboard for market trends and property performance.

---

## 📂 Project Structure

```text
Real-Estate-Hub/
├── Backend/                 # Spring Boot Application
│   ├── src/main/java        # Backend Logic (Controllers, Services, Models)
│   ├── src/main/resources   # Configuration (application.properties)
│   └── pom.xml              # Maven Dependencies
├── real-estate-frontend/    # React Application
│   ├── src/components       # Reusable UI Components
│   ├── src/services         # API Integration (Axios)
│   ├── public/              # Static Assets
│   └── package.json         # Frontend Dependencies
└── docs/                    # Extensive Documentation & Fix Logs
```

---

## 🛠️ Getting Started

### Prerequisites
- **Java 17**
- **Node.js** (v18+ recommended)
- **PostgreSQL**
- **Maven**

### 1. Backend Setup
1. Navigate to the `Backend` directory.
2. Configure your PostgreSQL database in `src/main/resources/application.properties`.
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### 2. Frontend Setup
1. Navigate to the `real-estate-frontend` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```

---

## 📖 Documentation
For detailed guides on specific features, deployment, or troubleshooting, please refer to the files in the `real-estate-frontend/docs/` directory.

---

## 🤝 Contributing
This project is under active development. Feel free to explore the roadmap and contribute to upcoming features!
