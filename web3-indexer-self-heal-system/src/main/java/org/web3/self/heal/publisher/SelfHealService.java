package org.web3.self.heal.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SelfHealService {
    public void publish(SelfHealMessagePayload payload) {
        // messages will be pusblished to a topic and then consumed by self-heal-system.
        // system will then automatically route these message to the appropriate topics based on the configuration of the topic
    }
}
