
### 포트 사용 정보
- 2181 : zookeeper
- 8081 : schema registry
    - depends_on : ~~broker1~~, broker2, ~~broker3~~
- ~~19092 : broker1~~
- 29092 : broker2
- ~~39092 : broker3~~
- 9000 : kafka-manager

> broker1, broker3 는 모두 주석처리해두었는데 필요하다면 주석 해제해서 사용하면 됨.

<br>

### 카프카 구동
```bash
source run-kafka-compose.sh
```

### 카프카 정지
```bash
source shutdown-kafka-compose.sh
```

