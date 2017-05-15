FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Mirko Bernardoni (mirko.bernardoni@ixxus.com)
LABEL name="inexus.ixxus.co.uk:28443/ixxus/hlv-search"
LABEL vendor="Ixxus"
LABEL description="Hasta la vista search"
LABEL version="1.0.0-SNAPSHOT"

VOLUME /tmp
ADD target/*.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xmx2500m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
