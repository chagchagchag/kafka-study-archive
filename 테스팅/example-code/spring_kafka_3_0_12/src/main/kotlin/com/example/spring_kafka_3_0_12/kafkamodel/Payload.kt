package com.example.spring_kafka_3_0_12.kafkamodel

import java.time.ZonedDateTime
import java.util.UUID

data class Payload(
    val userId: UUID,
    val name: String,
    val createdAt: ZonedDateTime,
)
