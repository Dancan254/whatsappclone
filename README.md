# WhatsApp Clone

A real-time messaging application built with Spring Boot, WebSocket, and Keycloak integration.

## Features

- **User Management**: Registration, authentication, and profile management
- **Real-time Messaging**: Instantaneous message delivery using WebSocket
- **Chat History**: View and search through past conversations
- **Media Sharing**: Send and receive images, videos, and audio
- **Status Tracking**: Know when messages are delivered and seen
- **Online Presence**: See when users are online or last active

## Technical Stack

- **Backend**: Spring Boot 3.4.4, Java 21
- **Database**: PostgreSQL
- **Authentication**: Keycloak (OAuth2/JWT)
- **Real-time Communication**: WebSocket/STOMP
- **API Documentation**: Swagger/OpenAPI
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 21 or later
- Docker and Docker Compose
- Maven 3.8+
- PostgreSQL client (optional, for direct DB access)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourname/whatsappclone.git
cd whatsappclone
```

### Start Dependencies with Docker

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5433
- Keycloak server on port 9090

### Build and Run the Application

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080

### Access Swagger Documentation

Once the application is running, you can access the API documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

## Project Structure

```
src/main/java/com/mongs/whatsappclone/
├── base/                  # Base classes for entities
├── chat/                  # Chat module
├── file/                  # File handling module
├── message/               # Message module
├── notification/          # Notification module
├── security/              # Security configuration
├── user/                  # User management
├── ws/                    # WebSocket configuration
└── WhatsappCloneApplication.java  # Main application class
```

## Security

This application uses Keycloak for authentication and authorization. The security is configured to:

- Use JWT tokens for API access
- Secure WebSocket connections
- Implement role-based access control

## Configuration

The application can be configured through `application.yml`. Key configuration items:

- Database connection details
- Keycloak integration settings
- File storage locations
- Logging settings

## API Endpoints

The application exposes the following main API endpoints:

- `/api/v1/users` - User management
- `/api/v1/chats` - Chat operations
- `/api/v1/messages` - Message operations
- `/api/v1/files` - File upload/download

WebSocket endpoint:
- `/ws` - For real-time messaging

## Testing the Application

For detailed instructions on how to test the application locally, including:
- Setting up Keycloak for user authentication
- Testing API endpoints with Postman
- Testing WebSocket connections
- Uploading and sharing media files

Please refer to our comprehensive [User Guide](USER_GUIDE.md).

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Acknowledgements

- Spring Boot and Spring Security teams
- Keycloak project
- PostgreSQL 