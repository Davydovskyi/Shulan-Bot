# Shulan-Bot

*Telegram bot - File sharing application*

## Application structure

The application has a microservice architecture.

![](./media/architecture.PNG "architecture")

### Modules:

- `dispatcher` - microservice for initial verification of incoming data and distribution of messages to appropriate
  queues in the RabbitMQ message broker;
- `node` - message processing microservice from RabbitMQ:
    - user registration;
    - saving messages from Telegram to the database;
    - message processing: **text**, **document**, **photo**;
    - saving file to database;
    - creating links to download files;
    - sending a response to the RabbitMQ;
- `mail-service` - microservice for sending an email with a link to confirm registration;
- `rest-service` - microservice for downloading files from the database using a link;
    - user activation confirmation;
    - downloading files from database;
- `common` - contains general code for interacting with the database and code for encrypting and decrypting identifiers;

### Database Structure:

![](./media/db_structure.PNG "db_structure")

### Common Stack:

- Java 21
- Spring Boot 3
- PostgreSQL
- RabbitMQ
- Docker
- Lombok

#### Dispatcher:

- Spring Web
- [Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots)<br>
  `Supports both Webhooks and Long Polling methods`<br>
  `For WebHooks I used the proxy server` [Serveo](https://serveo.net/)

#### Node:

- Spring Web
- Spring Data JPA
- Testcontainers : PostgreSQL
- Hypersistence Utilities Hibernate 60
- Jakarta Mail API
- Flyway

#### Mail Service:

- Spring Web
- Java Mail Service

#### Rest Service

- Spring Data JPA
- Spring Web

#### Common:

- Spring Data JPA
- Hashids

**Also, everything is ready for deployment on a VPS server, in particular on Beget**.