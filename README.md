# rate-limiting-service

Part of MVP of an e-commerce platform for home cooks. 

![obraz](https://github.com/user-attachments/assets/bd59b61f-5f0a-4ca7-bca4-2b3df865db3b)


Edge service:
   - Responsible for routing requests to upstream services.
     - #TODO Connecting to discovery service.
   - #TODO Handles rate limiting, retries and circuit breaking logic
     - #TODO Connecting Redis cluster
   - Logging requests data
   - #TODO Responisble for user authentication
     - #Connecting to OAuht and Keycloack
