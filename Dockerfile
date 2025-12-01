# Java 17 기반 이미지 사용
FROM eclipse-temurin:17-jdk-jammy

# JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]