FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/genreSubscriptions-1.0.0-SNAPSHOT.jar /app

EXPOSE 8085

CMD ["java", "-jar", "genreSubscriptions-1.0.0-SNAPSHOT.jar"]