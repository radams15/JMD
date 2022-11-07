FROM maven:3-eclipse-temurin-8

RUN apt-get update && apt-get install gcc make -y
