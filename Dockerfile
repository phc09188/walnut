FROM openjdk:11-jdk
COPY build/libs/walnut-0.0.1-SNAPSHOT.jar walnut.jar
ENTRYPOINT ["java", "-DSpring.profiles.active=prod", "-jar", "walnut.jar"]