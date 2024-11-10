FROM openjdk:8
MAINTAINER XinyuanXu
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

VOLUME /tmp

ADD *.jar app.jar

ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","app.jar"]

EXPOSE 8898
