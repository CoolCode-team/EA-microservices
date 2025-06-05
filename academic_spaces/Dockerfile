FROM eclipse-temurin:21 AS build

WORKDIR /app

COPY . .


RUN ./mvnw clean package -DskipTests

# Second stage: Create a lightweight runtime image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
