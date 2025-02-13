FROM openjdk:18
WORKDIR /app
COPY ./target/Employee-Service.jar /app
EXPOSE 5372
CMD ["java", "-jar", "Employee-Service.jar"]
