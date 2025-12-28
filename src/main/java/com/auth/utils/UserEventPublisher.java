package com.auth.utils;

import com.auth.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishUserCreated(User user) {
        try {
            Map<String, Object> event = new HashMap<>();

            event.put("eventType", "USER_CREATED");
            event.put("eventVersion", 1);
            event.put("eventId", UUID.randomUUID().toString());
            event.put("occurredAt", System.currentTimeMillis());

            Map<String, Object> userPayload = new HashMap<>();
            userPayload.put("id", user.getId());
            userPayload.put("email", user.getEmail());
            userPayload.put("fullName", user.getFullName());
            userPayload.put("tenantId", user.getTenantId());
            userPayload.put("plan", user.getPlan());
            userPayload.put("status", user.getStatus().name());

            event.put("user", userPayload);

            String json = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "auth.user.created",
                    user.getId().toString(),
                    json
            );

            System.out.println("USER_CREATED event published");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
