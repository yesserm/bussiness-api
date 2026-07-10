# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace

ARG MODULE_PATH
ARG ARTIFACT_ID

COPY . .

RUN --mount=type=cache,target=/root/.m2 chmod +x ./mvnw \
    && ./mvnw -pl "${MODULE_PATH}" -am -DskipTests package \
    && mkdir -p /workspace/build-output \
    && cp "${MODULE_PATH}/target/${ARTIFACT_ID}-"*.jar /workspace/build-output/app.jar

FROM eclipse-temurin:25-jre

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /workspace/build-output/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
