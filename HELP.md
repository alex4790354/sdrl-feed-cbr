# Read Me First

Micro-service for loading data from the RF Central Bank:
   - sdrl-feed-cbr: from RFCB into the RabbitMQ queue
   - sdrl-receiver-currency: from RabbitMQ queue into DB.


# Getting Started
1) Install Docker
2) Add containers from 'docker-compose.yml' (java-postgres-feed && rabbitmq)
3) Start application
4) Use 'SDRL Collection.postman_collection.json' in Postman or just run all or any URLs from list: 
    - http://localhost:8084/scripts/XML_val.asp?d=0
    - http://localhost:8084/scripts/XML_daily.asp?date_req=17/12/2022
    - http://localhost:8084/scripts/currency-rate/initial-load
    - http://localhost:8084/scripts/metalRate
    - http://localhost:8084/scripts/metalByDate?date_req=17/01/2023
    - http://localhost:8084/scripts/metal-rate/initial-load

5) Check: 
   - project data in DB: jdbc:postgresql://localhost:5431/feed         usr/pwd
   - result in **RabitMQ**: http://localhost:15672/  (guest/guest) 

### Reference sources: 

Source service urls:
- https://www.cbr.ru/scripts/XML_val.asp?d=0
- https://www.cbr.ru/scripts/XML_daily.asp?date_req=27/04/2023
- https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=01/01/2023&date_req2=31/12/2023&VAL_NM_RQ=R01235
- http://www.cbr.ru/scripts/xml_metall.asp?date_req1=17/01/2023&date_req2=17/01/2023


### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

