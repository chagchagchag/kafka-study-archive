package com.example.spring_kafka_3_0_12.kafkamodel

data class Field(
    val type: Type,
    val optional: Boolean,
    val field: String,
)
