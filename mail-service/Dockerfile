FROM alpine:3.20 AS base

RUN apk add git openjdk21 && apk cache clean

RUN git clone https://github.com/Davydovskyi/Shulan-Bot.git

WORKDIR /Shulan-Bot

RUN chmod +x gradlew && ./gradlew mail-service:bootJar

FROM alpine:3.20 AS result

RUN apk add openjdk21 && apk cache clean

WORKDIR /app
COPY --from=base /Shulan-Bot/mail-service/build/libs/mail-service-*.jar ./mail-service.jar

ENTRYPOINT ["java", "-jar", "mail-service.jar"]