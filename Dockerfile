FROM openjdk:8-jre-alpine
LABEL maintainer="danielvdhaak@gmail.com"

RUN apk add --no-cache bash libc6-compat

WORKDIR /
COPY wait-for-it.sh wait-for-it.sh
COPY target/data-challenge-1.0-SNAPSHOT-jar-with-dependencies.jar data-challenge.jar

CMD ./wait-for-it.sh -s -t 30 $APP_ZOOKEEPER_SERVER -- ./wait-for-it.sh -s -t 30 $APP_KAFKA_SERVER -- java -Xmx512m -jar data-challenge.jar