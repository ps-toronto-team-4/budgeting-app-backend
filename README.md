# budgeting-app-backend

## Dev requirements
You need postgres running on port 5432.
Make the password for the default user "admin123".

It is recommended to use postgres in a docker container:

```sh
docker pull postgres
docker run -name postgres-db -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -d postgres
```
