package com.example.spring_kafka_3_0_12

import com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserCreatedEventListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    // 1) 일반 문자열로 받을 경우
//    @KafkaListener(
//        id = "user-created-event-topic-consumer",
//        topics = ["user-created-event-topic"],
//    )
//    fun receiveByStringDataInRawString(
//        message: String
//    ){
//        logger.info("""
//            원본 메시지 (UserCreatedEvent) : $message
//        """.trimIndent())
//
//        val deserialized = nullableObjectMapper.readValue<UserCreatedEvent>(message, UserCreatedEvent::class.java)
//        logger.info("""
//            변환 메시지 (UserCreatedEvent) : $deserialized
//        """.trimIndent())
//    }

    // 2) 일반 객체를 Spring Messaging 에서 제공하는 기본 어노테이션인 @Payload 를 통해 받을 경우
//    @KafkaListener(
//        id = "user-created-event-topic-consumer",
//        topics = ["user-created-event-topic"],
//    )
//    fun receiveByStringDataInPayload(
//        @Payload message: String
//    ){
//        logger.info("""
//            원본 메시지 : $message
//        """.trimIndent())
//
//        val deserialized = nullableObjectMapper.readValue<UserCreatedEvent>(message, UserCreatedEvent::class.java)
//        logger.info("""
//            변환 메시지 : $deserialized
//        """.trimIndent())
//    }

    // 3) ConsumerRecord 로 받을 경우
//    @KafkaListener(
//        id = "user-created-event-topic-consumer",
//        topics = ["user-created-event-topic"],
//    )
//    fun receiveByConsumerRecord(
//        consumerRecord: ConsumerRecord<String, String>
//    ){
//        logger.info("""
//            원본 메시지 (UserCreatedEvent) : $consumerRecord
//        """.trimIndent())
//
//        val message = consumerRecord.value()
//
//        try{
//            val deserialized = nullableObjectMapper.readValue<UserCreatedEvent>(message, UserCreatedEvent::class.java)
//            logger.info("""
//                변환 메시지 (UserCreatedEvent) : $deserialized
//            """.trimIndent())
//        } catch (e: Exception){
//            logger.error(e.message, e)
//            e.printStackTrace()
//        }
//    }

    @KafkaListener(
        id = "user-created-event-topic-consumer",
        topics = ["user-created-event-topic"],
    )
    fun receiveByStringDataInRawString(
        message: UserCreatedEvent
    ){
        try{
            logger.info("""
                원본 메시지 (UserCreatedEvent) : $message
            """.trimIndent())
        }
        catch (e: Exception){
            logger.error(e.message, e)
            e.printStackTrace()
        }
    }

}