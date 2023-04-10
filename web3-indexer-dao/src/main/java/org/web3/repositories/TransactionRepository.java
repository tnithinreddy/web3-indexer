package org.web3.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web3.entities.TransactionEntity;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, String> {
    List<TransactionEntity> findByFromAddressOrToAddress(String fromAddress, String ToAddress);
}
