package com.example.spring_kafka_3_0_12

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer (
    val kafkaTemplate: KafkaTemplate<String, String>
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    fun send(topic: String, payload: String){
        val msg = StringBuilder()
            .append("sending payload = $payload ")
            .append("to topic = $topic")
            .toString()

        logger.info(msg)

        kafkaTemplate.send(topic, payload)
    }
}