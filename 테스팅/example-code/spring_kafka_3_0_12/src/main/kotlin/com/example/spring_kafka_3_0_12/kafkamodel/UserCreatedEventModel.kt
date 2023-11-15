package com.example.spring_kafka_3_0_12.kafkamodel

import java.io.Serializable

data class UserCreatedEventModel(
    val schema: Schema,
    val payload: Payload,
): Serializable
