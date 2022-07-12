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
## Setting Up pgAdmin

pgAdmin will allow you to view and interact with the database from a web browser (similar to phpMyAdmin for MySQL). Setting this up is relatively simple, just run the following commands:

```sh
docker pull dpage/pgadmin4
docker run -p 9091:80 -e 'PGADMIN_DEFAULT_EMAIL=user@domain.com' -e 'PGADMIN_DEFAULT_PASSWORD=SuperSecret' -d dpage/pgadmin4
```

Now, pgAdmin will be running locally on [port 9091](http://localhost:9091).

The email is `user@domain.com` and the password is `SuperSecret`. When registering with the postgres database, you'll need to first find the ip address for the postgres-db container. Simply run the following and look for the IP_ADDRESS variable:

```sh
docker inspect postgres-db
```

Now, for the database, the user is `postgres`, the password is `admin123` and the ip address is what you noted above.