1. Why sub-modules over sub-packages in single module
Idea is that each sub-module itself is a separate deployable system as per the HLD, so wanted to break things.
2. web3-indexer-api module has the server which exposes APIs like get block, get transactions of a given address etc
3. web3-indexer-blockchain-node-interface is created to abstract out the interactions with the node from our other modules. Also, the okhttp client is configured to ensure we retry atleast 3 times on every failure. back-off mechanisms are not yet added.
4. web3-indexer-block-manager subscribes to the node for fresh blocks and publishes messages to web3-indexer-transaction-manager
5. web3-indexer-transaction-manager process the incoming message and persists transactions, it further forwards the smart contract transactions to another processor within
6. web3-indexer-self-heal-system is not implemented yet, but it provides method to accept failure mesages from other systems
7. web3-indexer-liquibase manages the database DDL updates.


How to run the application ?
1. Clone the repo, go to root directory of the project and run `mvn clean install`
2. Create a database in postgresql by name `web3` (MySQL should work too, have not tested it)
3. Database schema's are managed using liquibase. Run following to create tables. (Replace following env variables) 
`java -jar -DDATASOURCE_URL=jdbc:postgresql://localhost:5432/web3 -DDATASOURCE_USERNAME=postgres -DDATASOURCE_PASSWORD=admin ./web3-indexer-liquibase/target/web3-indexer-liquibase-1.0-SNAPSHOT.jar
   `
4. To run the application 
`java -jar -DDATASOURCE_URL=jdbc:postgresql://localhost:5432/web3 -DDATASOURCE_USERNAME=postgres -DDATASOURCE_PASSWORD=admin ./web3-indexer-api/target/web3-indexer-api-1.0-SNAPSHOT.jar`
