FROM openjdk:17-jdk-slim

WORKDIR /app

COPY lib/ lib/
COPY run-server.sh .

RUN chmod +x run-server.sh

ENTRYPOINT ["./run-server.sh"]