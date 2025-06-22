# Peekashop

Peekashop is a small Spring Boot demo that uses RabbitMQ, PostgreSQL and Prometheus for metrics.

## Prerequisites

- **Java 21**
- **Maven**
- **Docker**

## Running with Docker Compose

Use `deploy.sh` to build and start the stack defined in `docker-compose.yml`:

```bash
./deploy.sh
```

The script runs `docker-compose` which reads the environment variables defined in `.env`:

- `DB_HOST` – database host (defaults to `postgres`)
- `DB_USER` – database user (defaults to `peekauser`)
- `DB_PASS` – database password (defaults to `peekapass`)

## Project modules

The main logic lives in the `order` package:

- **boundary** – REST controllers and `OrderQueueListener` which consumes RabbitMQ queues.
- **control** – service layer, including `OrderServiceImpl`, metrics (`OrderMetricsService`) and Rabbit listener factory configuration.
- **entity** – domain entities such as `Order` and JPA persistence models.

Metrics are collected with Micrometer and exposed to Prometheus. Queue listeners handle priority, realtime and bulk order messages with dedicated listener containers.

## Running tests

Execute all unit and integration tests with Maven:

```bash
./mvnw test
```

