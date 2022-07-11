# budgeting-app-backend

## Dev requirements
JDK 17 is required. If you are using IntelliJ, you can download it under project settings.

## Dev setup
You need postgres running on port 5432.
Make the password for the default user "admin123".

It is recommended to use postgres in a docker container:

```sh
docker pull postgres
docker run --name postgres-db -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres
```

Now clone and run this project and open with an IDE. IntelliJ is recommended.
Run main in com.sapient.BudgetingAppBackendApplication. The api endpoint will run at http://localhost:9090/graphql

## Dev testing
You can test the api using the graphical interface found at [/graphiql](http://localhost:9090/graphiql?path=/graphql)

## Entering the Database

Type the following in CMD/Powershell to enter the postgres cli:

```bash
docker exec -it postgres-db bash
psql -U postgres
```

Type the following to list all relations (Tables):

```sh
\dt
```
