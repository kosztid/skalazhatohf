# 1. Alapképet használunk (OpenJDK 17)
FROM eclipse-temurin:17-jdk-jammy

# 2. Munkakönyvtár beállítása a konténerben
WORKDIR /app

# 3. Másoljuk a JAR fájlt a konténerbe
COPY target/storage-0.0.1-SNAPSHOT.jar app.jar

# 4. Alkalmazás indításához szükséges parancs
ENTRYPOINT ["java", "-jar", "app.jar"]
