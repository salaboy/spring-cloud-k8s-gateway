FROM alpine:latest
ADD openjdk-11-ea+22_linux-x64-musl_bin.tar.gz /opt/jdk

ENV PATH=$PATH:/opt/jdk/jdk-11/bin

ENV PORT 8080
EXPOSE 8080
COPY target/*.jar /opt/app.jar
COPY appcds.classlist /opt/appcds.classlist


WORKDIR /opt
RUN ["java"," -Xshare:dump -XX:SharedClassListFile=/opt/appcds.classlist -XX:SharedArchiveFile=/opt/appcds.cache -jar app.jar"]

ENTRYPOINT exec java -Xshare:on -XX:+UseAppCDS -XX:SharedArchiveFile=/opt/appcds.cache $JAVA_OPTS -jar app.jar
