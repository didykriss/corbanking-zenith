FROM openjdk:17-jdk-alpine
MAINTAINER Tochukwu.Agada
COPY target/tochicba-0.0.1-SNAPSHOT.jar tochicba.jar.jar
ENTRYPOINT ["java","-jar","/tochicba.jar"]
