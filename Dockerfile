FROM maven:alpine

WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml dependency:resolve-plugins

COPY . .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml clean install -Dgoal=exec:java
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package
CMD ./wait-for-it.sh -s -t 30 $APP_ZOOKEEPER_SERVER -- ./wait-for-it.sh -s -t 30 $APP_KAFKA_SERVER -- mvn exec:java