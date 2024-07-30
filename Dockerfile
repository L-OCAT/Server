FROM openjdk:17
LABEL title="locat-server"
LABEL version="0.0.1"
LABEL authors="Team-LOCAT"

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod

CMD ["./gradlew", "clean", "build"]

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 443
ENTRYPOINT ["java","-jar","/app.jar"]
