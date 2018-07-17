FROM openjdk:8-jdk-alpine
ENV PORT 8080
EXPOSE 8080
COPY target/*.jar /opt/app.jar
WORKDIR /opt
RUN ["java", "-Xshare:dump"]

ENTRYPOINT exec java -Xshare:on $JAVA_OPTS -jar app.jar
