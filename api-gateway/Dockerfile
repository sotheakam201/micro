# --------------------------------------------
# 🐳 STAGE 1 — Build the JAR using Maven
# --------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the Maven files first to leverage Docker layer cache
COPY pom.xml .
COPY src ./src

# Run Maven build (skip tests if you want faster build for dev)
RUN mvn clean package -DskipTests

# --------------------------------------------
# 🐳 STAGE 2 — Run the JAR with OpenJDK only
# --------------------------------------------
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR built by the previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9091

ENTRYPOINT ["java", "-jar", "app.jar"]

