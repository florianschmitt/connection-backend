FROM dordoka/rpi-java8
VOLUME /tmp
ADD connection-backend-0.0.1-SNAPSHOT.jar target/app.jar
RUN sh -c 'touch target/app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom -jar target/app.jar" ]
