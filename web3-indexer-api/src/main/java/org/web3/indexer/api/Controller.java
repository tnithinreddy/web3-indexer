package org.web3.indexer.api;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3.block.manager.BlockManagerService;
import org.web3.indexer.response.models.Transaction;
import org.web3.indexer.service.TransactionReaderService;
import org.web3j.protocol.core.methods.response.EthBlock;

@RestController
@RequestMapping("/api/v1/")
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Controller {

    private final BlockManagerService blockManagerService;
    private final TransactionReaderService transactionReaderService;

    @GetMapping(value = "/block", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed(value = "GET.BLOCK", percentiles = {0.95, 0.99}) - ignoring the system monitoring for now
    public ResponseEntity<?> getBlockByNumber(@RequestParam long blockNumber) throws IOException {
        EthBlock block = blockManagerService.getBlock(blockNumber);
        log.info("block {}", block);
        return ResponseEntity.ok(block);
    }

    @GetMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactions(@RequestParam String address) {
        List<Transaction> transactions = transactionReaderService.getTransactionsOfAddress(address);
        return ResponseEntity.ok(transactions);
    }
}
