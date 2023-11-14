package com.example.spring_kafka_3_0_12.valueobject

import java.time.ZonedDateTime
import java.util.UUID

data class UserCreatedEvent (
    val userId: UUID,
    val name: String,
    val createdAt: ZonedDateTime,
){
}