<h3>Create a PostgreSQL instance with Docker:</h3>

```console
docker run --name ead-course -e POSTGRES_PASSWORD=course -e POSTGRES_USER=course -e POSTGRES_DB=ead-course -p 5432:5432 -d postgres
