package com.example.spring_kafka_3_0_12.objectmapper

import com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class OrderCreatedEventConverterTest {

    @Test
    fun `UserCreatedEvent 객체 직렬화, 역직렬화 테스트`(){
        val event = UserCreatedEvent(
            userId = UUID.randomUUID(),
            name = "ABC",
            createdAt = ZonedDateTime.now(),
        )

        val objectMapper = getObjectMapper()

        val serialized = objectMapper.writeValueAsString(event)
        println("serialized = $serialized")

        val deserialized = objectMapper.readValue<UserCreatedEvent>(serialized, UserCreatedEvent::class.java)
        println("deserialized = $deserialized")
    }

    fun getObjectMapper(): ObjectMapper{
        val module = KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, enabled = true)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()

        return ObjectMapper()
            .registerModule(module)
            .registerModule(JavaTimeModule())
    }
}