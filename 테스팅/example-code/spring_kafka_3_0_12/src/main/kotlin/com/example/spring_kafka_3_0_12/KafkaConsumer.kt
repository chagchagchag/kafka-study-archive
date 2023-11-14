package com.example.spring_kafka_3_0_12

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer {
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        id = "order-created-topic-consumer",
        topics = ["user-created-event-topic"]
    )
    fun receive(consumerRecord: ConsumerRecord<String,String>){
        val payload = consumerRecord.toString()
        val message = StringBuilder()
            .append("received payload = ").append(payload)
            .toString()

        logger.info(message)
    }

}