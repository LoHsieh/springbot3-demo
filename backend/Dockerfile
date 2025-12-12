FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven wrapper
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Expose application and debug ports
EXPOSE 8080 5005

# Run with Spring Boot DevTools for hot reload
# Debug port enabled for remote debugging
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]