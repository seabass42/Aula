# --- Build stage: compile the jar using Maven, not the local machine ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the pom first so Docker can cache the dependency download layer.
# As long as pom.xml doesn't change, `mvn dependency:go-offline` won't re-run
# on every build even if you've edited Java files.
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# --- Run stage: just a JRE + the built jar, no Maven, no source ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
