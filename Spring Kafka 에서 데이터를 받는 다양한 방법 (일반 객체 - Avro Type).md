## Spring Kafka 에서 데이터를 받는 다양한 방법 (일반 객체 - Avro Type)

Avro Json Schema 파일 없이 직접 작성한 `Type`, `Field`, `Schema`, `Payload` 로 데이터의 송수신을 할때에 대해 정리했다. 인프런 예제에서는 Type 은 없고 String 으로 표현, Field, Schema, Payload 를 구현한다. 중첩구조 역시 아니다. 단순한 객체 구조다. 그리고 이 필드들의 변환은 ModelMapper 로 수행한다. 아무래도 교육용 예제이기 때문에 복잡한 구현을 가리기 위해 ModelMapper 를 사용하지 않았나 싶다.

<br>



## 생산자 코드

`Type`, `Field`, `Schema`, `Payload` 는 모두 직접 작성한 타입이다.

```kotlin
@Component
class UserCreatedEventModelPublisher (
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
        sendBySchemaAndPayload(event = userCreatedEvent)
    }

    fun sendBySchemaAndPayload(event: UserCreatedEvent){
        val uuidType = Type(type = "string", logicalType = "uuid")
        val stringType = Type(type = "string", logicalType = "string")
        val microTimestampType = Type(type = "long", logicalType = "timestamp-micros")

        val fieldList = listOf(
            Field(type = uuidType, optional = false, field = "userId"),
            Field(type = stringType, optional = false, field = "name"),
            Field(type = microTimestampType, optional = false, field = "createdAt"),
        )

        val schema = Schema(
            type = "struct",
            fields = fieldList,
            optional = false,
            name = "UserCreatedEventModel"
        )

        val payload = Payload(
            userId = UUID.randomUUID(),
            name = "사용자 A",
            createdAt = ZonedDateTime.now(),
        )

        val eventModel = UserCreatedEventModel(schema, payload)
        val eventModelString = nullableObjectMapper.writeValueAsString(eventModel)

        kafkaProducer.send("user-created-event-model-topic", eventModelString)
    }
  	
  	// ...
  	
}
```





## Listener

### 1) @Payload 로 객체를 직접 받는 방식 : 에러 발생

```kotlin
@Component
class UserCreatedEventModelListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)
  	
  	// ...
    
  	// 1) @Payload 로 받는 방식 : 에러가 발생한다.
    @KafkaListener(
        id = "user-created-event-model-topic-consumer",
        topics = ["user-created-event-model-topic"],
    )
    fun receiveByPayloadEventModel(
        @Payload message: UserCreatedEventModel,
    ){
        logger.info("""
            원본 메시지 (UserCreatedEventModel) : $message
        """.trimIndent())
    }

}
```

<br>



### 2) 일반객체로 바로 받는 경우 : 에러 발생

```kotlin
@Component
class UserCreatedEventModelListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

  	// ...
  
    // 2) 에러가 발생한다.
    @KafkaListener(
        id = "user-created-event-model-topic-consumer",
        topics = ["user-created-event-model-topic"],
    )
    fun receiveByPlainEventModel(
        message : UserCreatedEventModel,
    ){
        logger.info("""
            원본 메시지 (UserCreatedEventModel) : $message
        """.trimIndent())
    }
  
  	// ...
}
```

<br>



### 3) @Payload String 타입으로 받은 후 Model 로 변환 (정상)

```kotlin
@Component
class UserCreatedEventModelListener (
    @Qualifier("nullableObjectMapper")
    val nullableObjectMapper: ObjectMapper
){
    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        id = "user-created-event-model-topic-consumer",
        topics = ["user-created-event-model-topic"],
    )
    fun receiveByStringData(
        @Payload message : String,
    ){
        logger.info("""
            원본 메시지 (UserCreatedEventModel) : $message
        """.trimIndent())

        val deserialized = nullableObjectMapper.readValue<UserCreatedEventModel>(message, UserCreatedEventModel::class.java)
        logger.info("""
            변환 메시지 (UserCreatedEventModel) : $deserialized
        """.trimIndent())
    }

}
```

