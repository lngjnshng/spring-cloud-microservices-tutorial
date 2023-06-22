FROM openjdk:21-ea-17-jdk-slim-buster
VOLUME /tmp
ADD order-1.0.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh
ENV JAVA_OPTS=""
ENTRYPOINT [ "/entrypoint.sh" ]