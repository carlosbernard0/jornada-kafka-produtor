package com.jornada.produtor.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public KafkaTemplate<String, String> config(){
        Map<String, Object> configuracoes = new HashMap<>();
        configuracoes.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configuracoes.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class);
        configuracoes.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);


        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configuracoes));
    }
}
