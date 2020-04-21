FROM openjdk:8
ADD build/libs/bonus_points_service.jar bonus_points_service.jar
EXPOSE 8081
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo:27017/local", "-jar", "bonus_points_service.jar"]
