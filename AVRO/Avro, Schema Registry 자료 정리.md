## Avro, Schema Registry 자료 정리

Java, maven 를 활용하는 자료

- [Avro and the schema Registry](https://aseigneurin.github.io/2018/08/02/kafka-tutorial-4-avro-and-schema-registry.html)
  - Schema Registry 를 구동하고 Schema 를 Register 하는 방법에 대해 정리한 문서
- [How to Use Schema Registry and Avro in Spring Boot Applications](https://www.confluent.io/blog/schema-registry-avro-in-spring-boot-application-tutorial/)
  - confluent cloud 환경에서 테스트해보는 문서
- [How to Work with Apache Kafka in Your Spring Boot Application](https://www.confluent.io/blog/apache-kafka-spring-boot-application/)
  - 별로 어렵지 않은 문서

<br>



Schema Registry 설명 및 사용법 

- [Introduction to Schema Registry in Kafka](https://medium.com/slalom-technology/introduction-to-schema-registry-in-kafka-915ccf06b902)
- [Schema Registry Overview](https://docs.confluent.io/platform/current/schema-registry/index.html)

<br>



gradle avro plugin

- [gradle-avro-plugin](https://github.com/davidmc24/gradle-avro-plugin)
- kotlin, gradle 을 사용한다면 maven 을 사용하지 않을 경우 [gradle-avro-plugin](https://github.com/davidmc24/gradle-avro-plugin) 플러그인을 사용하면 된다. 문서 내용이 약간 부족하기에 sourceSets 설정을 조금 수정해서 자동 생성된 코드를 프로젝트가 인식하도록 해줘야 한다.
- avro 는 원래 자동 생성 클래스를 생성해서 데이터 모델 구조를 공유한다. 추가 기능이 필요하다면, kotlin 에서 지원하는 확장 기능을 사용하거나 데이터 클래스, avro 클래스 사이의 어댑터 계층을 이용하면 된다.
- 자동 생성된 Java 클래스에는 getter, setter, builder 가 수없이 많아진다. 코틀린은 이런 점을 극복하기 위해 만들어졌다. (Java 로 Avro 를 사용할 경우 코드가 자동생성되는데 자동생성되는 코드에는 getter, setter, builder 가 수없이 많이 생성된다.)
- 코틀린의 관용구(idiom)들을 잘 활용하면, Java 에서 수없이 나타나는 보일러플레이트 코드 들을 피할 수 있다. 그리고 코드 생성 기능과 부가적인 빌드 과정 없이 KafkaAvroSerializer 와 함께 완벽하게 동작하게 된다. 

<br>













