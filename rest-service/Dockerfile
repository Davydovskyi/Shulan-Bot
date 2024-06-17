FROM alpine:3.20 AS base

RUN apk add git openjdk21 && apk cache clean

RUN git clone https://github.com/Davydovskyi/Shulan-Bot.git

WORKDIR /Shulan-Bot

RUN chmod +x gradlew && ./gradlew rest-service:bootJar

FROM alpine:3.20 AS result

RUN apk add openjdk21 && apk cache clean

WORKDIR /app
COPY --from=base /Shulan-Bot/rest-service/build/libs/rest-service-*.jar ./rest-service.jar

ENTRYPOINT ["java", "-jar", "rest-service.jar"]