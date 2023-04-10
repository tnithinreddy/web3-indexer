package org.web3.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web3.entities.BlockEntity;
import org.web3.entities.TransactionEntity;

@Repository
public interface BlockRepository extends CrudRepository<BlockEntity, String> {
    Optional<BlockEntity> findByBlockHash(String blockHash);
}