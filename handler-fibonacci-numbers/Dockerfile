FROM group-1/maven-jdk

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml package -DskipTests

ENTRYPOINT java -jar /home/app/target/*.jar

COPY rsyslog-client.conf /etc/rsyslog.d/rsyslog-client.conf

RUN apt-get update && apt-get install -y rsyslog