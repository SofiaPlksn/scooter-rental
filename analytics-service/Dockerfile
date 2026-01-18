FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S javauser && adduser -S javauser -G javauser
COPY target/*.jar app.jar
RUN chown javauser:javauser /app/app.jar
USER javauser
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]