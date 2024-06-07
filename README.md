# rate-limiting-service
This project is a part of bigger PaaS project, which is continuously growing. 


The idea is to create standalone rate limiting service. 

The Service is based on filter layer, that redirects requests to another address, securing the header with generated token.

The main principle is to provide limit of connections for a given IP. In the final version of app there will be few limiting algorithms. 

Persistence layer for metadata related to user request tokens will be based on Reddis server. Main reason for that is to provide fast, scalable and consistent service.

