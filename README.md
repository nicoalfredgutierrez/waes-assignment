# Waes assignment

# Tools used
- JDK 8u162
- gradle-5.2.1

## Assumptions and decisions

- In order to simplify the development and the project setup, Springboot was used.
- I decided to save the diff data in a memory database as it simplifies the development and the environment setup. As a drawback on application shutdown the data is lost.
- Hibernate is used to create the database. In a real environment I would use another tool like Flyway or LiquidBase to migrate the database.
- Environmental configuration is being saved as a resource in the artifact (application.properties), so it can run as a standalone app. For real developments, where the artifact goes through different environments, I would keep it out of the artifact in a configuration server or use one of the mechanisms provided by spring boot.
- Gradle was used to build the project. It allows me to easily create a new source set for the integration tests and it provides a wrapper to run the build without installing gradle on the machine.
- Springfox was integrated in order to generate the swagger documentation and provide info on how to consume the api.

## Application usage

- To run unit tests only: In console execute (Linux)./gradlew.sh test
- To run integration tests: In console execute (Linux)./gradlew.sh integrationTest (unit test are also run)
- To build the app: In console execute (Linux) ./gradlew.sh build. The jar will be created in <project_root>/build/libs
- To run the app: In console execute java -jar <project_root>/build/libs/assignment-1.0.0-SNAPSHOT.jar or ./gradlew bootRun. Once the app is up you can access http://localhost:8080/swagger-ui.html# to access the swagger documentation and try the endpoints with a ui.