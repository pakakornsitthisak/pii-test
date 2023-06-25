# Prerequisite
- Install SBT
- Add JRE

## Run/Build Locally

- In project root directory, run `docker-compose up` to start the PostgreSQL database
- Then, run the following commands:

```bash
sbt # start the sbt shell
run # run the app!
```

## Usage

- Make a `POST` request with the following body to `http://localhost:9000/login` to authenticate user and obtain the `auth_token`:

```bash
{
    "username": "admin",
    "password": "password"
}
```

- Make a `GET` request to `http://localhost:9000/users` with header `Authorization` with the `auth_token` value to list users
- Import the full Postman collection in /resource/piitest.postman_collection.json

## To run unittest
```
sbt test
```

## To run coverage report
```
sbt jacoco
```

- See coverage report in /target/scala-2.13/jacoco/report/html/index.html