# WhatsApp Chatbot Backend (Spring Boot)

## Features
- `/webhook` POST endpoint for WhatsApp Business API
- Replies with static message (mocked)
- Saves incoming messages to Firestore (mocked)
- `/api/test/send` endpoint for internal testing
- Firebase Admin SDK integration (basic config)
- JUnit5 unit tests
- Dockerfile for containerization
- Modular, clean structure

## Project Structure
- `controller/` - API endpoints
- `service/` - Business logic (WhatsApp + Firebase)
- `dto/` - Data objects
- `config/` - Firebase initialization

## Setup
1. Add your Firebase service account key JSON file.
2. Set config values in `application.properties`.

## Build & Run
```sh
./mvnw clean package
java -jar target/chatbot-0.0.1-SNAPSHOT.jar
```

## Docker
```sh
docker build -t whatsapp-chatbot .
docker run -p 8080:8080 whatsapp-chatbot
```

## Deploy to Render
- Connect this repo to Render
- Use Docker deploy
- Set environment variables as needed

---
**Note:** WhatsApp and Firestore logic is mocked for MVP. Extend as needed. 