package org.web3.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.web3.enums.BlockProcessingStatus;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "blocks")
@Data
public class BlockEntity extends BaseEntity {
    @Id
    String blockHash;

    @Column(nullable = false)
    BigInteger timestamp;

    @Column(nullable = false)
    int transactionsCount;

    @Column(nullable = false)
    BigInteger blockNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    BlockProcessingStatus status;
}
