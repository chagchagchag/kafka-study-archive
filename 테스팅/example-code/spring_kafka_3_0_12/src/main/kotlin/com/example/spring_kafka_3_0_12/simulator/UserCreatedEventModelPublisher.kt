package com.example.spring_kafka_3_0_12.simulator

import com.example.spring_kafka_3_0_12.KafkaProducer
import com.example.spring_kafka_3_0_12.kafkamodel.*
import com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.UUID

@Component
class UserCreatedEventModelPublisher (
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
        sendBySchemaAndPayload(event = userCreatedEvent)
    }

    fun sendBySchemaAndPayload(event: UserCreatedEvent){
        val uuidType = Type(type = "string", logicalType = "uuid")
        val stringType = Type(type = "string", logicalType = "string")
        val microTimestampType = Type(type = "long", logicalType = "timestamp-micros")

        val fieldList = listOf(
            Field(type = uuidType, optional = false, field = "userId"),
            Field(type = stringType, optional = false, field = "name"),
            Field(type = microTimestampType, optional = false, field = "createdAt"),
        )

        val schema = Schema(
            type = "struct",
            fields = fieldList,
            optional = false,
            name = "UserCreatedEventModel"
        )

        val payload = Payload(
            userId = UUID.randomUUID(),
            name = "사용자 A",
            createdAt = ZonedDateTime.now(),
        )

        val eventModel = UserCreatedEventModel(schema, payload)
        val eventModelString = nullableObjectMapper.writeValueAsString(eventModel)

        kafkaProducer.send("user-created-event-model-topic", eventModelString)
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