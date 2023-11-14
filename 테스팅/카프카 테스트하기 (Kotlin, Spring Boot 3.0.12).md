## 카프카 테스트하기 

이번문서에서는 카프카 테스트 하는 방식에 대해서만 정리. 겸사겸사 예제 코드도 추가 ㅋ

<br>



## 예제 프로젝트

예제 프로젝트는 코틀린, 스프링 부트 3.0.x 기반으로 선택.

<img src="./img/2023.11.14/1.png"/>

<br>



## 의존성

```kotlin
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.kafka:spring-kafka")

  // ...
  
  testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:kafka")
}
```

<br>





