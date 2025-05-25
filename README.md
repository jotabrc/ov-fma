**João Carlos Roveda Ostrovski**</br>
**System name**: ov-fma \- Finance Management Application (Aplicação de Gerenciamento Financeiro)  
**Version**: 71-SNAPSHOT

### Project description
RESTful API for tracking receipts and payments providing financial management control.  

#### Microservices:
1. **ov-fma-gateway**: Back end entrypoint, required for all services requests;
2. **ov-fma-user**: User registration/update, sends User information for 'ov-fma-auth' and 'ov-fma-finance';
3. **ov-fma-auth**: User authentication, receives user registration/update data from Kafka;
4. **ov-fma-finance**: Register payments, receipts keeping financial information decoupled from other services;
   1. save, update, delete.
5. **ov-fma-config**: Configuration server, retrieves services configurations from a private repository;
6. **kafka-service**: Decouples updates from 'ov-fma-user' to 'ov-fma-auth' and 'ov-fma-finance'.

### Settings
#### Required Environment Variables
##### ov-fma-gateway
.env:
```text
SECRET_KEY # for JWT decode
SPRING_PROFILES_ACTIVE=prd
CONFIG_SERVER_URI=config-service # Config server, if deploying with Docker Swarm 'config-service' will resolve to it's URI
CONFIG_SERVER_PORT=8084
CONFIG_SERVER_USER
CONFIG_SERVER_PASSWORD
```

##### ov-fma-user, ov-fma-finance & ov-fma-auth
Each services require its own '.env' file, place in the root folder of each module. e.g. 'ov-fma-*/.env'
```text
SECRET_KEY
SPRING_PROFILES_ACTIVE=prd
REDIS_PASSWORD
REDIS_PORT=6379
CONFIG_SERVER_URI=config-service
CONFIG_SERVER_PORT=8084
CONFIG_SERVER_USER
CONFIG_SERVER_PASSWORD
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_DB
POSTGRES_HOST=user-postgres-sql
POSTGRES_PORT=5432
```
##### ov-fma-config
.env
```text
SPRING_PROFILES_ACTIVE=prd
GIT_USERNAME
GIT_PASSWORD
REPO_URI
SPRING_USER
SPRING_PASSWORD
```
For more details see Docker Compose.
[docker-compose.yml](docker-compose.yml)

### Diagram
![diagram.png](docs/diagram.png)