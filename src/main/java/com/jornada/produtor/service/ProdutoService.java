package com.jornada.produtor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jornada.produtor.dto.MensagemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    @Value(value = "${kafka.topic}")
    private String topico;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void enviarMensagemAoTopico(MensagemDTO   mensagem, Integer particao) throws JsonProcessingException {
        //obj em string...
        String mensagemStr = new ObjectMapper().writeValueAsString(mensagem);

        MessageBuilder<String> stringMessageBuilder = MessageBuilder.withPayload(mensagemStr)
                .setHeader(KafkaHeaders.TOPIC, topico)
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString());
        if (particao != null){
            stringMessageBuilder.setHeader(KafkaHeaders.PARTITION, particao);
        }

        Message<String> mensagemParaKafka = stringMessageBuilder.build();

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(mensagemParaKafka);

        future.whenComplete(new BiConsumer<SendResult<String, String>, Throwable>() {
            @Override
            public void accept(SendResult<String, String> stringStringSendResult, Throwable throwable) {
                if (throwable != null){
                    log.error("ocorreu um erro ao enviar a mensagem {}, exception: {}", mensagem, throwable.getMessage());
                }else {
                    ProducerRecord<String, String> record = stringStringSendResult.getProducerRecord();
                    log.info("Mensagem enviada com sucesso: {} - {} ", record.key(), record.value());
                }
            }
        });
    }
}
