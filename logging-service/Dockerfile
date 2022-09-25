FROM ubuntu:18.04
RUN apt-get update && apt-get install -y systemd && apt-get install -y rsyslog
ADD rsyslog-service.conf /etc/rsyslog.d/rsyslog-client.conf
RUN service rsyslog start