FROM sbtscala/scala-sbt:graalvm-community-21.0.2_1.10.0_3.4.2
WORKDIR /app
COPY build.sbt /app/
COPY project /app/project
COPY src /app/src
RUN sbt update
RUN sbt compile
RUN sbt package
EXPOSE 8080
CMD ["sbt", "run"]
