package com.example.spring_kafka_3_0_12.config.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import java.io.Serializable

@Configuration
class KafkaConsumerConfig (
    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    val BOOTSTRAP_SERVERS: String,
    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    val AUTO_OFFSET_RESET: String,
    @Value("\${spring.kafka.consumer.enable-auto-commit}")
    val AUTO_COMMIT: Boolean,
){
    @Bean
    fun kafkaListenerContainerFactory(
        @Qualifier("kafkaConsumerFactory") kafkaConsumerFactory: ConsumerFactory<String, String>
    ) : ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.setConcurrency(3)
        factory.consumerFactory = kafkaConsumerFactory
        factory.containerProperties.pollTimeout = 500
        return factory
    }

    @Bean
    fun kafkaConsumerFactory() : ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(consumerConfig())
    }

    fun consumerConfig(): Map<String, Serializable>{
        return mapOf<String, Serializable>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to AUTO_OFFSET_RESET,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to AUTO_COMMIT,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        )
    }
}