# Before start the project:
edit the application.properties

spring.datasource.url=jdbc:postgresql://20.189.124.75:5432/springbootdb
spring.datasource.username=
spring.datasource.password=
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Run the project
mvn spring-boot:run

# Swagger UI URL
http://localhost:8080/swagger-ui/index.html