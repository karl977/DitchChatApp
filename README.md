# Ditch Chat App

This repository contains a hobby project built to improve knowledge of Spring framework, Vue, Typescript, Mockito and Testcontainers.

![](/screenshot.png "Screenshot of app")

# Features
- Single-page application 
- Registration and login
- Working chat with captured messages from real Twitch live broadcasts
  - Messages scraped with [Twitch Chat Scraper](https://github.com/karl977/TwitchChatScraper), which I made for this project as well
- Dark mode / Light mode
- CSRF protection
- Proper test coverage

# Used technologies & libraries
- Java & Spring framework
- Stomp messaging
- Vue 3 + Typescript
- PrimeVue

### Testing
- Testcontainers
- AssertJ
- Mockito

# Running
To run the application clone the repository and in root of project run:
```
docker compose up
```
Application will be available at http://localhost

# Developing
### Requirements
- OpenJDK 21 (other distributions will also probably work)
- Maven 3.9.3 (other versions will also probably work)
- Node (`frontend-maven-plugin` in this project uses version v18.18.2)
- Yarn (`frontend-maven-plugin` in this project uses version v1.22.19)

### Developing frontend
- In folder `src/main/app` run `yarn` to install dependencies
- Then run `yarn dev` for hot reloaded development

### Developing backend 
- Start development database container with `docker compose up` in `/docker` folder
- Configure IntelliJ to start/stop your application

# Testing
For testing run in project root:
```
mvn test
```

# Building a .jar
To build the application run in project root:
```
mvn clean package
```
The frontend is automatically built with `frontend-maven-plugin` which also installs front-end dependencies.
This means that there is no need to have Node and Yarn installed on the local machine for packaging.