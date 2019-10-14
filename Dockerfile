FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/expenses-0.0.1-SNAPSHOT-standalone.jar /expenses/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/expenses/app.jar"]
