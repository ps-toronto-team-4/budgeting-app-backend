# budgeting-app-backend

## Dev setup
You need postgres running on port 5432.
Make the password for the default user "admin123".

It is recommended to use postgres in a docker container:

```sh
docker pull postgres
docker run -name postgres-db -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres
```

Now clone and run this project and open with an IDE. IntelliJ is recommended.
Run main in com.sapient.BudgetingAppBackendApplication. The api endpoint will run at http://localhost:9090/graphql
