# budgeting-app-backend

## Dev requirements
JDK 17 is required. If you are using IntelliJ, you can download it under project settings.

## Dev setup

You need Docker Compose to get the database set up. If you're on Windows and have Docker Desktop, you're all set! Simply run the following to get Postgres and pgAdmin running:

```sh
docker compose up -d postgresdb pgadmin
```

Now, both Postgres and pgAdmin should be up, running, and confgured. The default username and password for the database is "postgres" and "admin123". The email and password for pgAdmin is "user@domain.com" and "SuperSecret".

Now clone and run this project and open with an IDE. IntelliJ is recommended.
Run main in com.sapient.BudgetingAppBackendApplication. The api endpoint will run at http://localhost:9090/graphql

## Dev testing
You can test the api using the graphical interface found at [/graphiql](http://localhost:9090/graphiql?path=/graphql)

## Entering the Database

Type the following in CMD/Powershell to enter the postgres cli:

```bash
docker exec -it postgresdb bash
psql -U postgres
```

Type the following to list all relations (Tables):

```sh
\dt
```
## Setting Up pgAdmin

pgAdmin will allow you to view and interact with the database from a web browser (similar to phpMyAdmin for MySQL). It should already be up and running on [port 9091](http://localhost:9091), you just need to register with the database to start querying it.

The email is `user@domain.com` and the password is `SuperSecret`. When registering with the postgres database, you can set the hostname/address to `postgresdb`, the user to `postgres`, and the password to `admin123`
