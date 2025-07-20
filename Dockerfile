FROM eclipse-temurin:21-jre
WORKDIR /app
RUN addgroup --system app && adduser --system --ingroup app app
COPY app.jar app.jar
USER app
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
