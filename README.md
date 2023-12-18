# Social Media Platform API

## Description

The Social Media Platform project aims to create a robust and user-friendly social networking application, providing individuals with a versatile and engaging platform for connecting, sharing, and communicating.

## User

### User Registration and Profiles

- **API Endpoint:** `POST /api/user/new`
- **_Roles:_**
  - Regular User (ROLE_USER)
  - Admin (ROLE_ADMIN)
  - Create accounts with unique usernames and email addresses and roles.
- **API Endpoint:** `PUT /api/user/update`

  - Edit and update user profiles pic and bio.

- **API Endpoint:** `Delete /api/user/remove/{userId}`

  - Log in using a username and password.

- **Admin Operations:**
  - **API Endpoint:** `Delete /api/user/remove/{userId}`delete user accounts.
  - **API Endpoint:** `GET /api/users/all`: View all user accounts.
  - **API Endpoint:** `Post/api/user/disable/{userId}`: Disable account

## Content Management

### Posting and Sharing

- **API Endpoint:** `POST /api/post/new`

  - Create posts with text, images, and videos.

- **API Endpoint:** `Delete/api/post/remove/{PostId}`

  - Delete post.

- **API Endpoint:** `POST /api/comment/new/{postId}/comment`
  - Add comments to posts.
- **API Endpoint:** `Delete /api/comment/remove/{postId}`

  - Add comments to posts.

- **API Endpoint:** `POST /api/post/reshare/{postId)`

  - Repost a post.

- **API Endpoint:** `POST /api/like/new/{postId)`
  - Like a post.
- **API Endpoint:** `Delete /api/like/remove/{postId)`
  - remove Like from post.

### Privacy Settings

- Set privacy settings for user posts (public, private).

### Media Storage

- **S3 Integration:**
  - Save files in S3, and store URLs in the required locations.

## Social Features

### Friendship and Following

- **API Endpoint:** `POST /api/follow/new/{username}`
  - Start Following.
- **API Endpoint:** `Delete /api/unfollow/{username}`
  - stop Following.

### Activity Feed

- **API Endpoint:** `GET /api/post/by/{userId}`
  - Get personalized activity feed.

### Messaging

- **API Endpoint:** `POST /api/chat/1v1`
  - Send private messages.
- **API Endpoint:** `POST /api/chat/group`
  - Send Group messages.

## Analytics

### User Analytics

- **API Endpoint:** `GET /api/analytic/likeCount/{postId}`
  - Get user interaction analytics likes.
- **API Endpoint:** `GET /api/analytic/shareCount/{postId}`
  - Get user interaction analytics shares.
- **API Endpoint:** `GET /api/analytic/commentCount/{postId}`
  - Get user interaction analytics comments.

## Technology Stack

### Backend

- Java (Spring Boot)

### Database

- Use MongoDB or Postgres ( I have used postgres )

## Getting Started

To get started with the Social Media Platform API, follow these steps:

1. Clone the repository.
2. Set up Database.
3. Run the project.
4. Use Postman for testing.

Happy coding!
