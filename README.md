# sdrl-feed-cbr

Scheduled Data Request and Loading. Microservice Demo project.

Micro-services load data from the RF Central Bank:
    - sdrl-feed-cbr: from RFCB into the RabbitMQ queue
    - sdrl-receiver-currency: from RabbitMQ queue into DB.


