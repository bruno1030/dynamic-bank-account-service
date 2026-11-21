package com.dynamicbank.account.kafka;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class AuditEventDTO {
    private String accountNumber;
    private UUID clientId;
    private EventTypeEnum eventType;
}
