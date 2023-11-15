package com.example.spring_kafka_3_0_12

import com.example.spring_kafka_3_0_12.kafkamodel.UserCreatedEventModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserCreatedEventModelListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        id = "user-created-event-model-topic-consumer",
        topics = ["user-created-event-model-topic"],
    )
    fun receiveByStringData(
        @Payload message : String,
    ){
        logger.info("""
            원본 메시지 (UserCreatedEventModel) : $message
        """.trimIndent())

        val deserialized = nullableObjectMapper.readValue<UserCreatedEventModel>(message, UserCreatedEventModel::class.java)
        logger.info("""
            변환 메시지 (UserCreatedEventModel) : $deserialized
        """.trimIndent())
    }

//    // 1) 에러가 발생한다.
//    @KafkaListener(
//        id = "user-created-event-model-topic-consumer",
//        topics = ["user-created-event-model-topic"],
//    )
//    fun receiveByPlainEventModel(
//        message : UserCreatedEventModel,
//    ){
//        logger.info("""
//            원본 메시지 (UserCreatedEventModel) : $message
//        """.trimIndent())
//    }

//    // 2) @Payload 로 받는 방식
//    @KafkaListener(
//        id = "user-created-event-model-topic-consumer",
//        topics = ["user-created-event-model-topic"],
//    )
//    fun receiveByPayloadEventModel(
//        @Payload message: UserCreatedEventModel,
//    ){
//        logger.info("""
//            원본 메시지 (UserCreatedEventModel) : $message
//        """.trimIndent())
//    }

}