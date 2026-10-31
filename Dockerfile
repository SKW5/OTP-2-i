FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        libgtk-3-0 \
        libgl1 \
        libxtst6 \
        fontconfig \
        fonts-noto-cjk \
    && fc-cache -f \
    && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src/main ./src/main
RUN mvn -B package -DskipTests

CMD ["mvn", "-B", "javafx:run"]
