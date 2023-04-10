package org.web3.self.heal.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SelfHealMessagePayload {
    Integer timestamp;
    String identifier;
    Object payload;
    String topic;
}
