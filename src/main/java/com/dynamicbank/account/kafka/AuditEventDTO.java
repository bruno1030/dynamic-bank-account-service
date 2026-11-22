package com.dynamicbank.account.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

// DTO usado pra criar o JSON que é transformado
// em mensagem (String) pra ser publicada no Kafka
@Getter
@AllArgsConstructor
public class AuditEventDTO {
    private UUID eventId;
    private EventTypeEnum eventType;
    private UUID clientId;
    private String accountNumber;
    private Instant occurredAt;
}
