# rate-limiting-service

Part of MVP of an e-commerce platform for home cooks. 


Edge service:
   - Responsible for routing requests to upstream services.
     - #TODO Connecting to discovery service.
   - #TODO Handles rate limiting, retries and circuit breaking logic
     - #TODO Connecting Redis cluster
   - Logging requests data
   - #TODO Responisble for user authentication
     - #Connecting to OAuht and Keycloack