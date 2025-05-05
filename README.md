# Web Quiz Engine

Et enkelt Spring Boot + Kotlin API for quiz. Utviklet for å bestå Hyperskills interne tester, legger til egne i framtiden.

## Kom i gang

### 1. Bygg prosjektet

    ./gradlew clean build

### 2. Start serveren
    ./gradlew bootRun

Serveren kjører nå på http://localhost:8080
### Registrer bruker

    curl -X POST http://localhost:8080/api/register \
      -H "Content-Type: application/json" \
      -d '{"email":"test@example.com", "password":"12345"}'

Prosjektet krever Java 17.
