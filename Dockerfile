FROM openjdk:17-oracle
ARG POSTGRES_IP
ENV ENV_POSTGRES_IP=$POSTGRES_IP
ADD target/budget-app-for-azure.jar budget-app-for-azure.jar
ENTRYPOINT java -jar budget-app-for-azure.jar "--spring.datasource.url=jdbc:postgresql://$ENV_POSTGRES_IP/postgres"
EXPOSE 9090