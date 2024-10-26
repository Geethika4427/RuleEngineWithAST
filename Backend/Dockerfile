# Use a base image with OpenJDK 17
FROM openjdk:17-slim

# Copy the JAR file from the target directory to /usr/app in the container
COPY target/ApplicationAST-0.0.1-SNAPSHOT.jar /usr/app/app.jar

# Set the working directory in the container
WORKDIR /usr/app

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
