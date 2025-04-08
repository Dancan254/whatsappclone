# WhatsApp Clone - Local Testing Guide

This guide will help you set up and test the WhatsApp Clone application locally using Postman, with a focus on setting up Keycloak for user authentication.

## Setup Prerequisites

- Java 21 installed
- Docker and Docker Compose installed
- Postman installed
- Git (to clone the repository)

## Step 1: Start the Infrastructure

First, start the required infrastructure using Docker Compose:

```bash
# Navigate to the project root
cd whatsappclone

# Start PostgreSQL and Keycloak containers
docker-compose up -d
```

Verify that both containers are running:

```bash
docker-compose ps
```

## Step 2: Set Up Keycloak

Since there's no UI for user registration, we need to set up Keycloak manually:

1. Access the Keycloak admin console at http://localhost:9090
2. Log in with the credentials:
   - Username: `admin`
   - Password: `admin`

### Create a New Realm

1. Hover over the dropdown in the top-left corner (showing "master")
2. Click "Create Realm"
3. Enter the name: `whatsapp_clone`
4. Click "Create"

### Create Client

1. In the left sidebar, go to "Clients"
2. Click "Create client"
3. Fill in the following:
   - Client ID: `whatsapp-app`
   - Client type: `OpenID Connect`
   - Click "Next"
4. Enable "Client authentication" and "Authorization"
5. Click "Next" and then "Save"

### Configure Client

1. Navigate to the "whatsapp-app" client you just created
2. Go to the "Settings" tab
3. Ensure "Access Type" is set to "confidential"
4. Set "Valid Redirect URIs" to `http://localhost:8080/*` and `http://localhost:4200/*`
5. Click "Save"

### Create User Roles

1. In the left sidebar, go to "Realm roles"
2. Click "Create role"
3. Enter "USER" as the role name
4. Click "Save"
5. Create another role named "ADMIN" following the same steps

### Create Test Users

1. In the left sidebar, go to "Users"
2. Click "Add user"
3. Fill in the following:
   - Username: `user1`
   - Email: `user1@example.com`
   - First name: `John`
   - Last name: `Doe`
   - Email verified: `ON`
   - Click "Create"

4. After creating the user, go to the "Credentials" tab
5. Set a password (e.g., `password`) and turn OFF "Temporary" 
6. Click "Set Password"

7. Go to the "Role mappings" tab
8. Under "Realm roles", select "USER" from the "Available roles" and click "Add selected"

Repeat these steps to create additional test users (e.g., `user2`, `user3`) with different names and emails.

## Step 3: Run the Application

Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application should now be running on port 8080.

## Step 4: Set Up Postman

### Import API Collection

1. Create a new Postman collection named "WhatsApp Clone"
2. Import the API specifications from Swagger (if available):
   - Go to http://localhost:8080/v3/api-docs
   - Copy the entire JSON
   - In Postman, go to File > Import > Raw text
   - Paste the JSON and import

### Set up Authentication

Create a request to get a token from Keycloak:

1. Create a new request in your collection
2. Name it "Get Token"
3. Set method to POST
4. URL: `http://localhost:9090/realms/whatsapp_clone/protocol/openid-connect/token`
5. Set the "Body" tab to "x-www-form-urlencoded"
6. Add the following key-value pairs:
   - `grant_type`: `password`
   - `client_id`: `whatsapp-app`
   - `username`: `user1` (or whichever user you created)
   - `password`: `password` (or the password you set)
   - `scope`: `openid`
   - `client_secret`: ``(client secret from the client you created)
7. Send the request, and you should receive a JSON response with an `access_token`

### Create a Postman Environment

1. Create a new environment named "WhatsApp Local"
2. Add a variable named "token" 
3. Create a script in the "Tests" tab of your "Get Token" request:

```javascript
if (pm.response.code === 200) {
    var jsonResponse = pm.response.json();
    pm.environment.set("token", jsonResponse.access_token);
}
```

This will automatically store the token in your environment variable.

## Step 5: Test API Endpoints

Now you can test the various endpoints. For each request, make sure to:

1. Set the Authorization type to "Bearer Token"
2. Use `{{token}}` as the token value to use the environment variable

### User Endpoints

#### Get Current User

- Method: GET
- URL: `http://localhost:8080/api/v1/users/me`

#### Get All Users

- Method: GET
- URL: `http://localhost:8080/api/v1/users`

### Chat Endpoints

#### Create a New Chat

- Method: POST
- URL: `http://localhost:8080/api/v1/chats`
- Body (JSON):
```json
{
    "recipientId": "user2-uuid"
}
```

#### Get User Chats

- Method: GET
- URL: `http://localhost:8080/api/v1/chats`

### Message Endpoints

#### Send a Text Message

- Method: POST
- URL: `http://localhost:8080/api/v1/messages`
- Body (JSON):
```json
{
    "chatId": "chat-id-here",
    "content": "Hello, this is a test message",
    "recipientId": "user2-uuid",
    "type": "TEXT"
}
```

#### Get Chat Messages

- Method: GET
- URL: `http://localhost:8080/api/v1/messages/chat/{chatId}`

## Step 6: Test WebSocket (Advanced)

Testing WebSockets in Postman requires the use of the WebSocket client feature:

1. Create a new WebSocket request
2. URL: `ws://localhost:8080/ws/websocket`
3. In "Headers" tab, add:
   - `Authorization`: `Bearer {{token}}`

For a better WebSocket testing experience, you can use specialized tools like [WebSocket King](https://websocketking.com/) or browser extensions.

### Sending a Message via WebSocket

Use the following format to send a message over WebSocket:

```json
{
  "destination": "/app/chat",
  "body": {
    "chatId": "chat-id-here",
    "content": "Hello via WebSocket",
    "recipientId": "user2-uuid",
    "type": "TEXT"
  }
}
```

## Step 7: Test File Upload

To test file upload:

1. Create a new POST request
2. URL: `http://localhost:8080/api/v1/files/upload`
3. Set the request body to "form-data"
4. Add a key "file" with type "File" and select a file from your computer
5. Send the request
6. The response will contain a file path that you can use when sending a message with media

```json
{
    "chatId": "chat-id-here",
    "content": "Check this image",
    "recipientId": "user2-uuid",
    "type": "IMAGE",
    "mediaPath": "path-from-upload-response"
}
```

## Troubleshooting

### Token Issues

If you're getting authentication errors:
- Ensure your token is valid and not expired (Keycloak tokens typically expire after 5 minutes)
- Run the "Get Token" request again to refresh your token
- Check that you've added the token correctly to your requests

### Database Issues

If you're having database connection issues:
- Verify that the PostgreSQL container is running
- Check that the database configuration in `application.yml` matches the Docker Compose setup

### Keycloak Issues

If you're having trouble with Keycloak:
- Make sure the realm name is exactly `whatsapp_clone`
- Verify that user roles are correctly assigned
- Check that the client is configured properly with client authentication enabled 