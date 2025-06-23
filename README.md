# WhatsApp Chatbot Backend (Spring Boot)

## Features
- `/webhook` POST endpoint for WhatsApp Business API and WhatsApp Cloud API
- Supports both flat and nested (Cloud API) webhook payloads
- Conversational agent with WhatsApp command parsing
- **Note CRUD**: Create, Read, Update, Delete, and List notes via REST API and WhatsApp messages
- Notes stored in Firebase Firestore
- Fallback reply for unknown commands
- Greeting/help command with usage instructions
- `/api/test/send` endpoint for internal testing
- Firebase Admin SDK integration
- JUnit5 unit tests (including webhook payload parsing, fallback, and help)
- Dockerfile for containerization
- Modular, clean structure

## Project Structure
- `controller/` - API endpoints (Webhook, Note REST)
- `service/` - Business logic (WhatsApp, Note, Firebase)
- `repository/` - Firestore CRUD logic
- `model/` - Data models (Note)
- `dto/` - Data transfer objects
- `config/` - Firebase initialization

## Setup
1. Configure Firebase credentials in one of these ways:
   - Set `FIREBASE_CREDENTIALS_JSON` environment variable with the service account JSON content
   - Set `FIREBASE_CREDENTIALS_PATH` environment variable with the path to your service account JSON file
   - Place the service account JSON file at the default path: `./wp-chatbot-86162-firebase-adminsdk-fbsvc-4d59e6e77b.json`

2. Set WhatsApp API configuration in `application.properties`:
   ```properties
   whatsapp.api.url=YOUR_WHATSAPP_API_URL
   whatsapp.api.token=YOUR_WHATSAPP_API_TOKEN
   ```

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

## REST API: Note CRUD
- `POST   /notes`         - Create a new note (`{"title": "Buy milk", "content": ""}`)
- `GET    /notes/{id}`    - Get a note by ID
- `PUT    /notes/{id}`    - Update a note (`{"content": "Call mom"}`)
- `DELETE /notes/{id}`    - Delete a note
- `GET    /notes`         - List all notes

## WhatsApp Webhook Payloads
### Flat format (legacy or test):
```json
{
  "from": "12345",
  "to": "67890",
  "message": "create note Buy milk"
}
```
### WhatsApp Cloud API format:
```json
{
  "entry": [
    {
      "changes": [
        {
          "value": {
            "messages": [
              {
                "from": "12345",
                "text": { "body": "create note Buy milk" }
              }
            ]
          }
        }
      ]
    }
  ]
}
```

## WhatsApp Commands
Send these messages to the bot:
- `create note Buy milk`
- `get note <id>`
- `update note <id> Call mom`
- `delete note <id>`
- `list notes`
- `hello`, `hi`, or `help` (shows help)

If the message is not recognized, the bot replies:
> Sorry, I didn't understand that. Try 'create note <title>' or 'list notes'.

## Testing
- Unit tests cover WhatsApp message handling, webhook payload parsing, fallback, and help/greeting responses.

---
**Note:** Firestore is used for persistent storage. All note operations are handled via Firestore. WhatsApp and REST endpoints are fully integrated. 