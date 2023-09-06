Kafka 에서 JsonSerializer 를 사용할 때는 jackson 의 Serializer 를 사용하지 말고 springframework.support 아래의 JsonSerializer 를 사용해야 한다.

```java
import org.springframework.kafka.support.serializer.JsonSerializer
```



딴거하다가 갑자기 카프카 쪽 일 다시 시작하면 또 이런 불상사가 일어날걸 알기에 정리해봤다.