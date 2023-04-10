package org.web3.transaction.retriever.decoder;


import com.fasterxml.jackson.databind.JsonNode;
import org.web3.transaction.retriever.store.ABIStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.core.methods.response.Log;

public class TransactionDecoder {

    // uses abi store to fetch abi of the contract
    @Autowired
    ABIStore abiStore;

    public JsonNode decodeInputData() {
        return null;
    }
    public JsonNode decodeContractLogs(String contractAddress, Log log) {
        // fetch ABI from the abi store
        // decode the transaction log
        // convert decoded log to a json object
        return null;
    }
}
