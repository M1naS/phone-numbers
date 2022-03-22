FROM maven:3.6.3 AS maven

WORKDIR /usr/backend
COPY . /usr/backend

RUN mvn package 

FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=phone-numbers-1.0.0.jar

WORKDIR /opt/backend
COPY --from=maven /usr/backend/target/${JAR_FILE} /opt/backend
COPY sample.db /opt/backend

ENTRYPOINT ["java","-jar","phone-numbers-1.0.0.jar"]