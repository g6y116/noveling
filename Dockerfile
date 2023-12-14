FROM openjdk:11-jdk
COPY build/libs/*.jar noveling.jar
ENTRYPOINT ["java", "-jar", "/noveling.jar"]