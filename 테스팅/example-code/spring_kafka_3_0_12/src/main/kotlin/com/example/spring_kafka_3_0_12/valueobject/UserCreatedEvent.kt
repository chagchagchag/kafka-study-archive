package com.example.spring_kafka_3_0_12.valueobject

import java.io.Serializable
import java.time.ZonedDateTime
import java.util.*

data class UserCreatedEvent (
    val userId: UUID,
    val name: String,
    val createdAt: ZonedDateTime,
) : Serializable {
}