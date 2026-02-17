# WebShop Microservice

A distributed microservices-based system built following best practices of event-driven and domain-driven architecture.

---

## 🏗 Architecture Overview

The system consists of three independent services and an API Gateway, with clear separation of responsibilities and a **database-per-service** approach.

### Communication

Services communicate:

- **Synchronously** via **gRPC (Protocol Buffers)**
- **Asynchronously** via **Apache Kafka** as the main event bus

Reliable event publishing is implemented using the **Outbox Pattern** combined with **Debezium (CDC)** to guarantee transactional consistency between the database and Kafka.

---

## 🧩 Technology Stack

### Core
- Spring Boot
- Spring Web
- Spring Data
- Spring Security

### Communication
- gRPC + Protobuf (service-to-service communication)
- Apache Kafka (event streaming)
- Apache Avro + Schema Registry (versioned event schemas)

### Data
- PostgreSQL (database per service)

### Infrastructure
- Docker
- Docker Compose (local orchestration)
- Kubernetes (production-ready orchestration support)

### Security
- JWT-based authentication and authorization through the API Gateway
- Access / Refresh token mechanism
- RSA-based asymmetric signing for secure token issuance and validation

---

## 🔄 Event-Driven Flow

1. Client → API Gateway
2. Gateway → Service (gRPC)
3. Service writes domain data + Outbox event in a single transaction
4. Debezium captures DB changes (CDC)
5. Event is published to Kafka
6. Other services consume and react asynchronously

---

## 🚀 Key Architectural Principles

- Microservices architecture
- Event-driven design
- Database per service
- Strongly typed contracts (Avro / Protobuf)
- Transactional messaging (Outbox + CDC)
- Containerized and reproducible deployment  
