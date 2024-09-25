FROM openjdk:21-jdk

ENV APP_NAME contas-0.0.1-SNAPSHOT
COPY "/target/${APP_NAME}.jar"  "/app/${APP_NAME}.jar"
WORKDIR /app
CMD java -jar ${APP_NAME}.jar
EXPOSE 8080