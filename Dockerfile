FROM openjdk:8-jdk-alpine
ENV PORT 8080
EXPOSE 8080
COPY target/*.jar /opt/app.jar
COPY appcds.classlist /opt/appcds.classlist

WORKDIR /opt
RUN ["java", "-Xshare:dump -XX:+UseAppCDS -XX:SharedClassListFile=/opt/appcds.classlist -XX:SharedArchiveFile=/opt/appcds.cache"]

ENTRYPOINT exec java -Xshare:on -XX:+UnlockCommercialFeatures -XX:+UseAppCDS -XX:SharedArchiveFile=/opt/appcds.cache $JAVA_OPTS -jar app.jar
