# Spring Music App

| ![main](https://github.com/user-attachments/assets/fdf2cc03-eef1-4144-927d-162244255f87) | ![profile](https://github.com/user-attachments/assets/83169f04-de42-4852-bacd-490a213a4b97) | ![signin](https://github.com/user-attachments/assets/a6688578-3ac2-4aef-846b-c52d17eea244) |
|:----------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:|
| Home                                                                                     | Profile                                                                                     | Sign In                                                                                     |

## Features

- User registration and authentication with Spring Security
- User profile management, including following other users
- Uploading, editing, and deleting songs
- Adding songs to favorites
- Searching songs by keywords
- Commenting on songs using WebSocket

## Used Technologies:

- Spring (Boot, Data, Security, WebSocket)
- JPA / Hibernate
- PostgreSQL
- Thymeleaf
- Maven
- Junit, Mockito
- Lombok

## How to run

1. **Build the project:**
    ```sh
    mvn clean package
    ```
    
2. **Build Docker images:**
    ```sh
    docker-compose build
    ```
    
3. **Start the application:**
    ```sh
    docker-compose up -d
    ```
    
4. **Access the application:**

   Once the application is running, you can access it at [http://localhost:8080](http://localhost:8080).
