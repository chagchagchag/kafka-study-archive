package com.example.spring_kafka_3_0_12.kafkamodel

data class Schema(
    val type: String,
    val fields: List<Field>,
    val optional: Boolean,
    val name: String,
)
