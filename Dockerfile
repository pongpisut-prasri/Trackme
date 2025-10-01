FROM openjdk:21-oraclelinux8

WORKDIR /app/trackme
CMD ["./build.sh"]
COPY /target/trackmeh.foodtracking-0.0.1-SNAPSHOT.jar trackme.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","trackme.jar"]