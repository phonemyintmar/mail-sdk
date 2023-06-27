package com.pm.mailsdk.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "mail-sender.config")
//@PropertySource(value = "file:./config/mail-sender-${spring.profiles.active}.properties")
@Getter
@Setter
public class KafkaConfig {

    private static final String bootstrapServers = "10.201.xx.xx";

    //make static classes like in kafka producer and make it available if you want
    private Map<String, Object> properties;

    @Bean("mailProducerFactory")
    public ProducerFactory<String, Object> mailProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.putAll(properties);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "mailKafkaTemplate")
    public KafkaTemplate<String, Object> mailKafkaTemplate(
            @Qualifier("mailProducerFactory") ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
