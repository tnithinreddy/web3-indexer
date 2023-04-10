package org.web3.transaction.retriever;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3.self.heal.publisher.SelfHealMessagePayload;
import org.web3.self.heal.publisher.SelfHealService;
import org.web3j.protocol.core.methods.response.EthBlock;

@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class SmartContractTransactionProcessor {

    private final SelfHealService selfHealService;


    @Retry(name = "SMART_CONTRACT_TXN_PROCESSOR_RETRY_POLICY", fallbackMethod = "processSmartContractMessageFallback")
    private void processSmartContractMessage(EthBlock.TransactionObject transaction) {
        // this will interact with ABI store, decode the input data of smart contract.
        // fetch events emitted within smart contract and index them in a separate table
    }

    private void processSmartContractMessageFallback(EthBlock.TransactionObject transaction, RuntimeException re) {
        log.error("error processing transaction with hash {}, exception {}", transaction.getHash(), re);
        SelfHealMessagePayload selfHealMessagePayload = SelfHealMessagePayload.
                builder()
                .topic("smart-contract-transaction-topic")
                .identifier(transaction.getHash())
                .payload(transaction)
                .build();
        selfHealService.publish(selfHealMessagePayload);
    }

    public void process(EthBlock.TransactionObject transaction) {
        processSmartContractMessage(transaction);
    }
}
