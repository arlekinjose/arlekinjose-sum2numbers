


# sumtwonumbers-api

REST API for getting the sum of two numbers.

A RESTful API created using Spring Boot. We have used PostgreSQL as the relational database and JdbcTemplate to interact with that.
Apart from this, we have used JSON Web Token (JWT) to add authentication. Using JWT, we can protect certain endpoints and ensure that user must be logged-in to access those.

The code of the Spring Boot is based on the youtube course teached by Beau Carnes of freeCodeCamp.org which you can access here - [Youtube Playlist](https://www.youtube.com/playlist?list=PLWieu6NbbqTwwYwylgXmmKVX1ZWsUVx8m)

The ansible playbooks used the following roles

https://github.com/geerlingguy/ansible-role-pip
https://github.com/geerlingguy/ansible-role-docker
https://github.com/geerlingguy/ansible-role-postgresql

## Setup and Installation on local machine

1. **Clone the repo from GitHub**
   ```sh
   git clone https://github.com/arlekinjose/arlekinjose-sum2numbers.git
   cd sumtwonumbers-api
   ```
2. **Spin-up PostgreSQL database instance**

   You can use either of the below 2 options:
   - one way is to download from [here](https://www.postgresql.org/download) and install locally on the machine
   - another option is by running a postgres docker container:
     ```sh
     docker container run --name postgresdb -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
     ```
3. **Create database objects**

   In the root application directory (sumtwonumbers-api), SQL script file (sumtwonumbers_db-docker.sql) is present for creating all database objects
   - if using docker (else skip this step), first copy this file to the running container using below command and then exec into the running container:
     ```
     docker container cp sumtwonumbers_db-docker.sq postgresdb:/
     docker container exec -it postgresdb bash
     ```
   - run the script using psql client:
     ```
     psql -U postgres --file sumtwonumbers_db-docker.sq
     ```
4. **(Optional) Update database configurations in application.properties**
   
   If your database is hosted at some cloud platform or if you have modified the SQL script file with some different username and password, update the src/main/resources/application.properties file accordingly:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/sumtwonumbersdb
   spring.datasource.username=sumtwonumbers
   spring.datasource.password=password
   ```
5. **Run the spring boot application**
   ```sh
   ./mvnw spring-boot:run
   ```
   this runs at port 8080 and hence all enpoints can be accessed starting from http://localhost:8080

   ## Setup and Installation on AWS (South America Region) using a pipeline in Gitlab.com
   Create the following variables in a project in gitlab.com, e.g   https://gitlab.com/arlekinjose/arlekinjose-sum2numbers.git

   DB_APP_PASSWORD               --> Password of app database user "sumtwonumbers" in Postgres  
   GITLAB_TOKEN                  --> Valid Deploy token on Gitlab.com project
   PG_PASSWORD                   --> Password of admin database user "postgres"
   SSH_KEY                       --> Private key to access EC2 instances that will by used by Ansible
   TF_VAR_aws_access_key_id      --> AWS access key
   TF_VAR_aws_secret_access_key  --> AWS secret access key

By running the pipeline 
- terraform will create two spot virtual machines, a VPC network, Security Groups, a Route table
a gateway, and two Ip elastic addresses, 
- Ansible will install Postgres in one VM
- It will create the app docker image tar file 
- Ansible will install docker, python pip3 
- Ansible will copy the tar file to the docker VM, create the docker image, and run the app docker container listening on port 8080.

The public IP can be seen on AWS console, pipeline logs or in the file artifacts of the gitlab pipeline.

REST API Endpoints
http://APP_PUBLIC_IP:8080/api/users/register
http://APP_PUBLIC_IP:8080/api/users/login
http://APP_PUBLIC_IP:8080/api/users/login
http://APP_PUBLIC_IP:8080/api/sums

Postman collection sample: 

{"info":{"_postman_id":"45e8ed29-4ae0-42d4-8475-5dbdb93d82a5","name":"devops-challenge","schema":"https://schema.getpostman.com/json/collection/v2.0.0/collection.json"},"item":[{"name":"registerUser","id":"8cecd129-c27c-4a8c-a0e2-e7d09ca5ba8d","request":{"method":"POST","header":[{"key":"Authorization","value":"Bearer JWT_TOKEN","type":"text","disabled":true}],"body":{"mode":"raw","raw":"{\n    \"firstName\": \"Omar\",\n    \"lastName\": \"alcala\",\n    \"email\": \"Omar@gmail.com\",\n    \"password\": \"test123\"\n}","options":{"raw":{"language":"json"}}},"url":"http://APP_PUBLIC_IP:8080/api/users/register","description":"devops-challenge"},"response":[]},{"name":"loginUser","id":"b1f34d53-c60a-455e-8693-9246fb1eb994","request":{"method":"POST","header":[{"key":"Authorization","value":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjIyNDYzOTQsImV4cCI6MTYyMjI1MzU5NCwidXNlcklkIjoxLCJlbWFpbCI6Im1hdXJpY2lvQGdtYWlsLmNvbSIsImZpcnN0TmFtZSI6Ik1hdXJpY2lvIiwibGFzdE5hbWUiOiJNZWRpbmEifQ.v9An8I31BlSMKOyl7O8m-Wrox2ek9ZxSz85URD9P7xY","type":"text","disabled":true}],"body":{"mode":"raw","raw":"{\n    \"email\":\"Omar@gmail.com\",\n    \"password\":\"test123\"\n}","options":{"raw":{"language":"json"}}},"url":"http://APP_PUBLIC_IP:8080/api/users/login"},"response":[]},{"name":"createSum","id":"b44537c0-a727-4bfd-ad87-b821f6baac69","request":{"method":"POST","header":[{"key":"Authorization","value":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjI1NDk1OTMsImV4cCI6MTYyMjU1Njc5MywidXNlcklkIjoxLCJlbWFpbCI6Im9tYXJAZ21haWwuY29tIiwiZmlyc3ROYW1lIjoiT21hciIsImxhc3ROYW1lIjoiYWxjYWxhIn0.Kg2Qtho5NWijyJa0Q4VZB8iHwQy3lCftTj1__Mexw-Y","type":"text"}],"body":{"mode":"raw","raw":"{\n    \"sum1\": 6,\n    \"sum2\": 4\n}","options":{"raw":{"language":"json"}}},"url":"http://APP_PUBLIC_IP:8080/api/sums"},"response":[]},{"name":"getAllSums","id":"5df8634c-7d93-4ada-a968-5059a411e52c","request":{"method":"GET","header":[{"key":"Authorization","value":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjI1NDk1OTMsImV4cCI6MTYyMjU1Njc5MywidXNlcklkIjoxLCJlbWFpbCI6Im9tYXJAZ21haWwuY29tIiwiZmlyc3ROYW1lIjoiT21hciIsImxhc3ROYW1lIjoiYWxjYWxhIn0.Kg2Qtho5NWijyJa0Q4VZB8iHwQy3lCftTj1__Mexw-Y","type":"text"}],"url":"http://APP_PUBLIC_IP:8080/api/sums"},"response":[]}]}

