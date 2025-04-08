# WhatsApp Clone - System Architecture

## Overview
This document outlines the architectural design of the WhatsApp Clone application, which is a real-time messaging platform built with Spring Boot and WebSocket technology.

## System Components

### Backend Architecture
The application follows a layered architecture approach:

1. **Presentation Layer**
   - REST Controllers (`*Controller.java`) - Handle HTTP requests
   - WebSocket Endpoints - Handle real-time messaging

2. **Service Layer**
   - Business Logic Components (`*Service.java`) 
   - Data transformation with mapper classes (`*Mapper.java`)

3. **Data Access Layer**
   - JPA Repositories (`*Repository.java`)
   - Named Queries for complex data operations

4. **Domain Layer**
   - Entity models (User, Chat, Message)
   - DTOs for data transfer (`*Response.java`, `*Request.java`)

### Security Architecture
- OAuth2 Resource Server using Keycloak
- JWT-based authentication
- Role-based authorization

### Messaging Architecture
- WebSocket for real-time communication
- STOMP protocol as messaging sub-protocol
- Message broker for routing messages to specific users

## Core Modules

### User Management
- User registration and authentication through Keycloak
- User profile management
- User status tracking (online/offline)

### Chat Management
- Creating new conversations
- Listing user conversations
- Retrieving chat history

### Message Management
- Sending/receiving text messages
- Media sharing (image, video, audio)
- Message status tracking (sent, seen)

### File Management
- Upload/download of media files
- Storage of files on server filesystem

## Database Schema

### Users Table
- User profile information
- Authentication details
- Timestamps for last seen/activity

### Chats Table
- References to participants (sender and recipient)
- Created and modified timestamps
- One-to-many relationship with messages

### Messages Table
- Content and type (text, image, video, audio)
- Status (sent, seen)
- References to sender, receiver, and chat
- Media path for non-text messages

## External Integrations

### Keycloak
- Authentication server
- User management
- Token issuance and validation

### PostgreSQL
- Persistent data storage
- Transaction management

## Technical Stack

- **Programming Language**: Java 21
- **Framework**: Spring Boot 3.4.4
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Authentication**: Keycloak/OAuth2
- **Real-time Communication**: WebSocket/STOMP
- **API Documentation**: Swagger/OpenAPI
- **Containerization**: Docker

## Deployment Architecture

```
┌─────────────────┐          ┌─────────────────┐
│                 │          │                 │
│  Spring Boot    │◄─────────┤    Keycloak     │
│  Application    │          │                 │
│                 │          │                 │
└────────┬────────┘          └─────────────────┘
         │
         │
         ▼
┌─────────────────┐
│                 │
│   PostgreSQL    │
│   Database      │
│                 │
└─────────────────┘
```

## Data Flow

1. **Authentication Flow**
   - User authenticates through Keycloak
   - JWT token is returned to the client
   - Token is used for subsequent API calls

2. **Messaging Flow**
   - User connects to WebSocket with JWT
   - Messages are sent through STOMP protocol
   - Server routes messages to specific users
   - Message status is updated in database

3. **Media Sharing Flow**
   - Media is uploaded via REST API
   - File is stored on server filesystem
   - Message with media reference is sent via WebSocket
   - Recipient downloads media via REST API 