1. Why sub-modules over sub-packages in single module
Idea is that each sub-module itself is a separate deployable system as per the HLD, so wanted to break things.
2. web3-indexer-api module has the server which exposes APIs like get block, get transactions of a given address etc
3. web3-indexer-blockchain-node-interface is created to abstract out the interactions with the node from our other modules. Also, the okhttp client is configured to ensure we retry atleast 3 times on every failure. back-off mechanisms are not yet added.
4. web3-indexer-block-manager subscribes to the node for fresh blocks and publishes messages to web3-indexer-transaction-manager
5. web3-indexer-transaction-manager process the incoming message and persists transactions, it further forwards the smart contract transactions to another processor within
6. web3-indexer-self-heal-system is not implemented yet, but it provides method to accept failure mesages from other systems
7. web3-indexer-liquibase manages the database DDL updates.


How are failures handled ?
1. Every interaction to the block chain node happens with retries. 
2. Every interaction between `block-manager` and `transaction-manager`, between `transaction-manager` and `smart-contract-message-processor` 
uses `resilience4j` retry mechanism. Also if it fails after retries, then messages are published to `self-heal-system`

Why use messaging over http interactions ?
1. Message queues help in handling back pressure
2. Helps in retrying the messages at later point in time. Http requests cannot be retried without explicitly parsing and saving them as messages elsewhere.
3. Retries can happen even after system/service re-starts
4. Since processing of transactions within a block takes time, http requests can time out. 
Block request(netty for example) threads for so long will scale the system

How to run the application ?
1. Clone the repo, go to root directory of the project and run `mvn clean install`
2. Create a database in postgresql by name `web3` (MySQL should work too, have not tested it)
3. Database schema's are managed using liquibase. Run following to create tables. (Replace following env variables) 
`java -jar -DDATASOURCE_URL=jdbc:postgresql://localhost:5432/web3 -DDATASOURCE_USERNAME=postgres -DDATASOURCE_PASSWORD=admin ./web3-indexer-liquibase/target/web3-indexer-liquibase-1.0-SNAPSHOT.jar
   `
4. To run the application 
`java -jar -DDATASOURCE_URL=jdbc:postgresql://localhost:5432/web3 -DDATASOURCE_USERNAME=postgres -DDATASOURCE_PASSWORD=admin ./web3-indexer-api/target/web3-indexer-api-1.0-SNAPSHOT.jar`

To Access the application over HTTP:

By default the server runs on `8080` port.

To fetch all transactions of a given address (includes from and to of the transaction)
`curl --location 'localhost:8080/api/v1/address?address={address_here}'`

To fetch a given block and inspect its JSON
`curl --location 'localhost:8080/api/v1/block?blockNumber={block_number_here}'`
