# Rating System

## Project Description
Rating System is an independent rating system for in-game item sellers (CS:GO, FIFA, Dota, Team Fortress, etc.). The rating is formed based on user-submitted comments, which are reviewed by administrators. The project's main goal is to create a list of top sellers across various gaming categories.

## User Roles
1. **Administrator** - Reviews seller registration requests, moderates comments.
2. **Seller** - Can register, manage their profile, and receive reviews.
3. **Anonymous User** - Can leave comments without registration.

## Core Features
- User registration and authentication.
- Adding sellers and their profiles.
- Leaving comments on seller profiles.
- Calculating seller ratings based on reviews.
- Comment moderation by administrators.

## API Endpoints
### Registration and Authentication
- `POST /auth/register` - Register a new user.
- `POST /auth/confirm` - Confirm email with verification code.
- `POST /auth/login` - Authenticate a user.
- `POST /auth/forgot_password` - Request password reset (sends a verification code to email).
- `POST /auth/reset` - Reset password using verification code.

### User Management
- `GET /users/rating` - Get a list of sellers sorted by rating.
- `GET /users/{userId}/comments` - Get comments left by a user.
- `GET /users/{userId}/comments/{commentId}` - Get a particular comment left by a user.
- `GET /users/{userId}/received-comments` - Get comments left about a user.
- `POST /users/{userId}/comments` - Leave a comment about a seller.
- `DELETE /users/{userId}/comments/{commentId}` - Delete a comment (author only).
- `PATCH /users/{userId}/comments/{commentId}` - Edit a comment (author only).

### Game Object Management
- `POST /users/{userId}/objects` - Add a game object.
- `GET /users/{userId}/objects` - Get a seller's game objects.
- `PATCH /users/{userId}/objects/{objectId}` - Update game object details (author only).
- `DELETE /users/{userId}/objects/{objectId}` - Delete a game object (author only).

### Administrative Functions
- `GET /admin/comments/verify` - Get a list of comments for verification.
- `POST /admin/comments/{commentId}/verify` - Approve a comment.
- `GET /admin/users/verify` - Get a list of users pending verification.
- `POST /admin/users/{userId}/verify` - Approve a user.

## Technologies
- **Backend**: Java 21, Spring Boot, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Caching**: Redis (for storing verification codes)
- **Authentication**: JWT Token
- **Logging**: SLF4J + Logback
- **Testing**: JUnit, Mockito
