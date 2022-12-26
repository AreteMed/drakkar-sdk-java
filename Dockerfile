ARG IMAGE=359743534547.dkr.ecr.us-east-2.amazonaws.com/gradle:jdk11
FROM $IMAGE

RUN mkdir /home/gradle/project

COPY secring.kbx /home/gradle/project

COPY gradle.properties.local /home/gradle/project

WORKDIR /home/gradle/project

CMD ["gradle"]
