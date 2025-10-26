FROM openjdk:21
COPY . /server
WORKDIR /server
RUN ./mvnw package -DskipTests
RUN mv target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]