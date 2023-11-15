package com.example.spring_kafka_3_0_12.kafka

import com.example.spring_kafka_3_0_12.UserCreatedEventModelListener
import com.example.spring_kafka_3_0_12.KafkaProducer
import com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = [
    "listeners=PLAINTEXT://localhost:9092",
    "port=9092"
])
class EmbeddedKafkaIntegrationTest1 {

    @Autowired
    lateinit var userCreatedEventModelListener: UserCreatedEventModelListener

    @Autowired
    lateinit var kafkaProducer: KafkaProducer

    @Autowired  @Qualifier("nullableObjectMapper")
    lateinit var objectMapper: ObjectMapper

    val topicName = "user-created-event-topic"

    @Test
    fun `EmbeddedKafka 기반 단순 프로듀서, 컨슈머의 전송,수신 테스트`(){
        val userId = UUID.randomUUID()
        val data = UserCreatedEvent(
            userId = userId,
            name = "Alphabet",
            createdAt = ZonedDateTime.now(),
        )

        val message = objectMapper.writeValueAsString(data)
        kafkaProducer.send(topicName, message)

        val spyConsumer = Mockito.spy(userCreatedEventModelListener)

//        Mockito.verify(spyConsumer).receive()

    }
}