package com.dynamicbank.account.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditProducer {

   private final KafkaTemplate<String, String> kafkaTemplate;

   @Value("${dynamicbank.kafka.topics.audit}")
   private String auditTopic;

   public void sendEvent(String key, String payload) {
      kafkaTemplate.send(auditTopic, key, payload)
              .whenComplete((result, ex) -> {
                 if (ex == null) {
                    log.info("Evento enviado com sucesso. Tópico: {} | Partição: {} | Offset: {}",
                            auditTopic,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                 } else {
                    log.error("Erro ao enviar evento para o tópico {}", auditTopic, ex);
                 }
              });
   }

}
