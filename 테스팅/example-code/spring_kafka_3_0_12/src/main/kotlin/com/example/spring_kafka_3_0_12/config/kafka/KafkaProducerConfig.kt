package com.example.spring_kafka_3_0_12.config.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.io.Serializable

@Configuration
class KafkaProducerConfig (
    @Value("\${spring.kafka.producer.bootstrap-servers}")
    val BOOTSTRAP_SERVERS: String,
){
    @Bean
    fun kafkaTemplate(
        @Qualifier("kafkaProducerFactory")
        kafkaProducerFactory: ProducerFactory<String, String>
    ): KafkaTemplate<String, String> {
        return KafkaTemplate(kafkaProducerFactory)
    }

    @Bean
    fun kafkaProducerFactory() : ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory<String, String>(producerConfig())
    }

    fun producerConfig() : Map<String, Serializable> {
        return mapOf<String, Serializable> (
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
    }
}