## Spring Kafka 에서 데이터를 받는 다양한 방법 (일반객체 (Non - Avro))

내용의 핵심을 위해서 설정코드나 이런 것들은 생략했다. 기본 소스 코드는 [여기](https://github.com/chagchagchag/kafka-study-archive/tree/main/%ED%85%8C%EC%8A%A4%ED%8C%85/example-code/spring_kafka_3_0_12)에 정리해두었다. <br>

메시지큐를 통해 데이터 송수신을 통해 데이터를 교환하려 할 때에는 가장 심플한 방법은 문자열로 송신하고 문자열로 수신해서 양측에서 합의하고 있는 타입으로 역직렬화를 하면 된다. 간단하고 빠르게 처리가 필요한 코드이면서 내부 개발 도구로만 사용할 경우에는 굳이 타입화를 한다고 시간을 오래 걸려가면서  작업할 필요가 없다. 양측에서 문자열로 송수신하고 합의된 타입으로 직렬화/역직렬화를 하면 된다.<br>

만약 시스템이 커져서 각 부서가 타입이 달라질수도 있고 여러가지 안전장치를 해두고자할 경우는 컴파일 타임에서 체크가 되도록 타입화를 Avro를 통해 할 수 있다. 이렇게 데이터 송수신시에 타입을 서로 호환되게끔 할 때 Avro를 사용하는 경우는 Kafka를 사용할 경우에 한해서다. 

참고로 RabbitMQ에서는 이런 처리 없이 바로 특정 객체 타입으로 바로 바로 직렬화 역직렬화가 된다. 물론 커스텀하게 몇몇 ObjectMapper 등의 설정을 추가해서 사용하지만 Kafka 만큼 복잡하지는 않다.<br>

<br>



## 생산자 코드

생산자에서는 아래와 같이 데이터를 발생시킨다고 해보자. 1초에 한번씩 `UserCreatedEvent` 를 발생시키고 있다. 그리고 생성된 이벤트는 `user-created-event-topic` 으로 전송하고 있다.

```kotlin
@Component
class UserCreatedEventPublisher (
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

        sendByStringValue(event = userCreatedEvent)
    }

    fun sendByStringValue(event: UserCreatedEvent){
        val eventDataInString = nullableObjectMapper.writeValueAsString(event)
        kafkaProducer.send("user-created-event-topic", eventDataInString)
    }

		// ... 중략 ... 
  	
}
```

<br>



## 소비자측

### 1) 문자열로 직렬화되어 전송된 데이터를 문자열로 받은 후 역직렬화

```kotlin
// ...

@Component
class UserCreatedEventListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    // 1) 일반 문자열로 받을 경우
    @KafkaListener(
        id = "user-created-event-topic-consumer",
        topics = ["user-created-event-topic"],
    )
    fun receiveByStringDataInRawString(
        message: String
    ){
        logger.info("""
            원본 메시지 (UserCreatedEvent) : $message
        """.trimIndent())

        val deserialized = nullableObjectMapper.readValue<UserCreatedEvent>(message, UserCreatedEvent::class.java)
        logger.info("""
            변환 메시지 (UserCreatedEvent) : $deserialized
        """.trimIndent())
    }
  
  	// ...

}
```

이 경우 정상적으로 잘 수행된다.

<br>



출력결과

```plain
sending payload = {"userId":"ae5a7c01-3a26-498f-bad8-f2ea8bee611c","name":"[C@74ddcca2[C@607afd03[C@2354fce4","createdAt":1700071315.900954100} to topic = user-created-event-topic
원본 메시지 (UserCreatedEvent) : {"userId":"ae5a7c01-3a26-498f-bad8-f2ea8bee611c","name":"[C@74ddcca2[C@607afd03[C@2354fce4","createdAt":1700071315.900954100}
변환 메시지 (UserCreatedEvent) : UserCreatedEvent(userId=ae5a7c01-3a26-498f-bad8-f2ea8bee611c, name=[C@74ddcca2[C@607afd03[C@2354fce4, createdAt=2023-11-15T18:01:55.900954100Z[UTC])
```

<br>



### 2) 일반 객체를 Spring Messaging 의 @Payload 를 통해 String 형태로 받은 후 역직렬화

```kotlin
// ...

import org.springframework.messaging.handler.annotation.Payload

@Component
class UserCreatedEventListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    // 2) 일반 객체를 Spring Messaging 에서 제공하는 기본 어노테이션인 @Payload 를 통해 받을 경우
    @KafkaListener(
        id = "user-created-event-topic-consumer",
        topics = ["user-created-event-topic"],
    )
    fun receiveByStringDataInPayload(
        @Payload message: String
    ){
        logger.info("""
            원본 메시지 : $message
        """.trimIndent())

        val deserialized = nullableObjectMapper.readValue<UserCreatedEvent>(message, UserCreatedEvent::class.java)
        logger.info("""
            변환 메시지 : $deserialized
        """.trimIndent())
    }

}
```

<br>



이 경우 역시 정상적으로 수행된다.

```plain
sending payload = {"userId":"ed5ae770-4376-4ca4-aaca-d70893b480c5","name":"[C@7aad157b[C@1836d15[C@43f8bc4a","createdAt":1700071455.756026700} to topic = user-created-event-topic
원본 메시지 : {"userId":"ed5ae770-4376-4ca4-aaca-d70893b480c5","name":"[C@7aad157b[C@1836d15[C@43f8bc4a","createdAt":1700071455.756026700}
변환 메시지 : UserCreatedEvent(userId=ed5ae770-4376-4ca4-aaca-d70893b480c5, name=[C@7aad157b[C@1836d15[C@43f8bc4a, createdAt=2023-11-15T18:04:15.756026700Z[UTC])
```

<br>



### 3) ConsumerRecord 로 받을 경우

```kotlin
// ...

import org.apache.kafka.clients.consumer.ConsumerRecord

// ...

@Component
class UserCreatedEventListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

  	// ...
  	
    // 3) ConsumerRecord 로 받을 경우
    @KafkaListener(
        id = "user-created-event-topic-consumer",
        topics = ["user-created-event-topic"],
    )
    fun receiveByConsumerRecord(
        consumerRecord: ConsumerRecord<String, String>
    ){
        logger.info("""
            원본 메시지 (UserCreatedEvent) : $consumerRecord
        """.trimIndent())

        val message = consumerRecord.value()

        try{
            val deserialized = nullableObjectMapper.readValue<UserCreatedEvent>(message, UserCreatedEvent::class.java)
            logger.info("""
                변환 메시지 (UserCreatedEvent) : $deserialized
            """.trimIndent())
        } catch (e: Exception){
            logger.error(e.message, e)
            e.printStackTrace()
        }
    }

}
```

<br>



출력결과

```plain
sending payload = {"userId":"f6dd09cc-692e-4a2d-b71e-56304e191c87","name":"[C@1ec03864[C@430fdf2f[C@4d92395d","createdAt":1700071659.008442700} to topic = user-created-event-topic
원본 메시지 (UserCreatedEvent) : ConsumerRecord(topic = user-created-event-topic, partition = 0, leaderEpoch = 0, offset = 118, CreateTime = 1700071659008, serialized key size = -1, serialized value size = 125, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = {"userId":"f6dd09cc-692e-4a2d-b71e-56304e191c87","name":"[C@1ec03864[C@430fdf2f[C@4d92395d","createdAt":1700071659.008442700})
변환 메시지 (UserCreatedEvent) : UserCreatedEvent(userId=f6dd09cc-692e-4a2d-b71e-56304e191c87, name=[C@1ec03864[C@430fdf2f[C@4d92395d, createdAt=2023-11-15T18:07:39.008442700Z[UTC])
```



### 4) 일반객체 (UserCreatedEvent) 로 kafkaListener에서 직접 받을 경우 (에러!!)

이 경우 에러를 낸다. 

수신하는 코드는 아래와 같다.

```kotlin
// ...

import com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent

// ...

@Component
class UserCreatedEventListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)
  
  	// ... 
  
    @KafkaListener(
        id = "user-created-event-topic-consumer",
        topics = ["user-created-event-topic"],
    )
    fun receiveByStringDataInObject(
        message: UserCreatedEvent
    ){
        try{
            logger.info("""
                원본 메시지 (UserCreatedEvent) : $message
            """.trimIndent())
        }
        catch (e: Exception){
            logger.error(e.message, e)
            e.printStackTrace()
        }
    }

}
```

<br>

이 경우 에러를 낸다.<br>

출력결과<br>

```plain
중략 ...
Caused by: org.springframework.messaging.converter.MessageConversionException: Cannot convert from [java.lang.String] to [com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent] for GenericMessage [payload={"userId":"d2216d55-90fd-4924-84f3-df4e43d29fee","name":"[C@dbec46d[C@6ac1be9f[C@2e7c8ad9","createdAt":1700071856.736690700}, headers={kafka_offset=125, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@7c4aead7, kafka_timestampType=CREATE_TIME, kafka_receivedPartitionId=0, kafka_receivedTopic=user-created-event-topic, kafka_receivedTimestamp=1700071856755, kafka_groupId=user-created-event-topic-consumer}]
	at org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver.resolveArgument(PayloadMethodArgumentResolver.java:151) ~[spring-messaging-6.0.13.jar:6.0.13]
	
...
```

<br>

에러 문구를 자세히 보면 `Cannot convert from [java.lang.String] to [com.example.spring_kafka_3_0_12.valueobject.UserCreatedEvent]` 라는 문구가 보인다.<br>

@KafkaListener 에서 직렬화를 못하는 문제다. 카프카는 참 불성실하다. RabbitMQ 의 @RabbitListener 에서는 이런 일이 없었다.하하하.

아무튼... 이런 문제로 인해... 카프카 사용시에 뭔가 데이터 송수신 시 컴파일타임에 미리 정해져있는 타입으로 받으려 할 경우에는 Avro 를 사용하는 편인 것 같다.



