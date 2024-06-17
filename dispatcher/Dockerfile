FROM alpine:3.20 AS base

RUN apk add git openjdk21 && apk cache clean

RUN git clone https://github.com/Davydovskyi/Shulan-Bot.git

WORKDIR /Shulan-Bot

RUN chmod +x gradlew && ./gradlew dispatcher:bootJar

FROM alpine:3.20 AS result

RUN apk add openjdk21 && apk cache clean

WORKDIR /app
COPY --from=base /Shulan-Bot/dispatcher/build/libs/dispatcher-*.jar ./dispatcher.jar

ENTRYPOINT ["java", "-jar", "dispatcher.jar"]