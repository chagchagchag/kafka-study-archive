## Avro 를 사용하는 경우 vs 필요없는 경우

개인적으로 Spring Cloud + DDD 프로젝트 진행하면서 Avro 까지 건드리게 됐는데, Avro가 필요한 경우와 아닌 경우를 정리해보기로 했다. 사실 엄청 작은 규모의 프로젝트이기에 Avro 까지는 필요없는데 이왕 백수일때 예제라도 만들어두자 하면서 avro 를 정리해두고 있다. 내가 왜 굳이 이런 고생을 흑...

<br>



## 참고

- Java 사용시에는 클래스를 생성하는 별도의 빌드 스텝을 정의해야 한다. maven 으로도 가능하고 gradle로도 가능하다.

- 참고로 코틀린을 사용할 때는 avro 사용시에 별도의 빌드 스텝을 정의하지 않아도 된다는 막강한 편리함이 발생한다는 사실을 어제 새벽에 알게됐다. 이것 역시 문서로 정리하는 데에는 부가적으로 고통스러움이 수반되겠다 싶다는 생각이 들었지만 정리를 하고 있다.

<br>



## Avro 를 사용하는 경우

- 자료형을 json 명세를 통해 다양한 언어로 공유하고자 하는 경우
- avro 의 schema 표준을 따라서 공통된 자료형을 서로 다른 플랫폼에서 메시지 큐를 통해 교환하려는 경우
- avro 사용없이 이미 만들어진 산출물이 있을때 고도화를 조금 더 정형화해서 이루고자 할 경우

<br>



## Avro 가 필요없는 경우

- 이미 확고하게 정해둔 자료형이 팀내에 존재하는 경우
- 기타 부수적인 개발 리소스를 소모하고 싶지 않을 때
- Java/Kotlin 타입 → Jackson 직렬화 → 메시지큐 → Jackson 역직렬화 → 소비자 와 같이 이미 정해진 규칙이 있다면 굳이 피요하진 않다.

<br>



## 내생각

주로 Avro 를 사용하게 되는 경우는 java 외에도 python, scalar, javascript 등과 같은 언어와 다양한 프레임워크와 공존할 때 자꾸 개발자끼리 싸우는 경향이 발생한다. 그리고 그 중 비아냥 대거나 말을 함부로 하는 분도 가끔... 많이? 있다 ㅋㅋ. 이런 경우 그냥 apache 표준 스키마인 avro 표준 스키마를 따르는게 제일 권장되는 방식인 것 같다.

<br>



