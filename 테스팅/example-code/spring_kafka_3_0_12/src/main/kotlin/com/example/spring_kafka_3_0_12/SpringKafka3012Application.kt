package com.example.spring_kafka_3_0_12

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SpringKafka3012Application

fun main(args: Array<String>) {
	runApplication<SpringKafka3012Application>(*args)
}
