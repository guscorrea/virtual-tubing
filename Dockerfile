FROM openjdk:8-jre-alpine
COPY target/virtual-tubing-1.0.0.jar virtual-tubing-1.0.0.jar
ENTRYPOINT ["java","-jar","/virtual-tubing-1.0.0.jar"]