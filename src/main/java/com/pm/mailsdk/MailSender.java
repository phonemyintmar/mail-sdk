package com.pm.mailsdk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class MailSender {

    private static final String topic = "mails-to-send";
    private static KafkaTemplate<String, Object> kafkaTemplate;

    public MailSender(KafkaTemplate<String, Object> kafkaTemplate) {
        MailSender.kafkaTemplate = kafkaTemplate;
    }

    public static void send(Object request) {
        //save log if need
        kafkaTemplate.send(topic, request).addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("[Topic={}] Error {}", topic, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("[Topic={}] Send message = [{}] with offset [{}]", topic,
                        result.getProducerRecord().value().toString(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
