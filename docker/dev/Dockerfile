# Stage 1: Build the application
FROM maven:3-eclipse-temurin-17 AS build-dependency

ARG WORKDIR=/opt/app

WORKDIR ${WORKDIR}
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Extract file jar for application image
FROM eclipse-temurin:17-jre-alpine as builder
ARG WORKDIR=/opt/app
ARG JAR_FILE=${WORKDIR}/target/*.jar

WORKDIR ${WORKDIR}
COPY  --from=build-dependency ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

# Stage 3: Create the final image
FROM eclipse-temurin:17-jre-alpine
ARG WORKDIR=/opt/app
WORKDIR ${WORKDIR}

# Set the timezone.
ENV TZ=Asia/Ho_Chi_Minh
RUN set -x \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

RUN addgroup twoup && adduser -S twoup -G twoup
RUN chown -R twoup:twoup /opt/app
USER twoup

COPY --from=builder ${WORKDIR}/dependencies/ ./
COPY --from=builder ${WORKDIR}/spring-boot-loader/ ./
COPY --from=builder ${WORKDIR}/snapshot-dependencies/ ./
COPY --from=builder ${WORKDIR}/application/ ./

# Default JVM options — overridable từ docker-compose qua biến JAVA_OPTS
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -Xms256m \
  -Xmx512m \
  -XX:MaxMetaspaceSize=256m \
  -Xss512k \
  -XX:+ExitOnOutOfMemoryError"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
