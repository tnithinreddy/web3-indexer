package org.web3.indexer.node;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.reactivex.Flowable;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NodeInterface {

    private final Web3j web3j;

    @CircuitBreaker(name = "CircuitBreakerService")
    public EthBlock ethGetBlockByNumber(BigInteger blockNumber, boolean returnFullTransactionObjects) throws IOException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber),
                returnFullTransactionObjects).send();
    }

    @CircuitBreaker(name = "CircuitBreakerService")
    public Optional<TransactionReceipt> ethGetTransactionReceipt(String txnHash) throws IOException {
        return web3j.ethGetTransactionReceipt(txnHash).send().getTransactionReceipt();
    }

    @CircuitBreaker(name = "CircuitBreakerService")
    public EthGetCode ethGetCode(String address, DefaultBlockParameter defaultBlockParameter) throws IOException {
        return web3j.ethGetCode(address, defaultBlockParameter).send();
    }

    public Flowable<EthBlock> blockFlowable(boolean fullTransactionObjects) {
        return web3j.blockFlowable(false);
    }

}
