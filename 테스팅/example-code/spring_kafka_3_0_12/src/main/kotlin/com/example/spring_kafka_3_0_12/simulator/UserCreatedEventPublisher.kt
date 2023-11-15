package com.example.spring_kafka_3_0_12.simulator

import com.example.spring_kafka_3_0_12.KafkaProducer
import com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

@Component
class UserCreatedEventPublisher (
    val kafkaProducer: KafkaProducer,
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper,
){

    @Scheduled(
        initialDelayString = "3000",
        fixedDelayString = "1000",
    )
    fun scheduleUserCreatedEvent(){
        val userCreatedEvent = UserCreatedEvent(
            userId = UUID.randomUUID(),
            name = getRandomName(),
            createdAt = ZonedDateTime.now()
        )

        // 1)
        sendByStringValue(event = userCreatedEvent)
    }

    fun sendByStringValue(event: UserCreatedEvent){
        val eventDataInString = nullableObjectMapper.writeValueAsString(event)
        kafkaProducer.send("user-created-event-topic", eventDataInString)
    }

    fun getRandomName() : String{
        return StringBuilder()
            .append(getRandomAlphabet())
            .append(getRandomAlphabet())
            .append(getRandomAlphabet())
            .toString()
    }

    fun getRandomAlphabet() : String{
        val t = LocalDateTime.now().nano % 60
        return Character.toChars(t).toString()
    }
}