FROM openjdk:17-oracle
ADD target/budget-app-for-azure.jar budget-app-for-azure.jar
ENTRYPOINT ["java", "-jar","budget-app-for-azure.jar"]
EXPOSE 9090