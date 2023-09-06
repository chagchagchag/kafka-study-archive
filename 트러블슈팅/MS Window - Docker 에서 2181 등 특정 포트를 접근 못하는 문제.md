# MS Window - Docker 에서 2181 등 특정 포트를 접근 못하는 문제

### 참고자료

- [Cannot start service backend: Ports are not available: listen tcp 0.0.0.0:3001: bind: An attempt was made to access a socket in a way forbidden by its access permissions. 에러 해결하기](https://velog.io/@dom_hxrdy/Cannot-start-service-backend-Ports-are-not-available-listen-tcp-0.0.0.03001-bind-An-attempt-was-made-to-access-a-socket-in-a-way-forbidden-by-its-access-permissions.-%EC%97%90%EB%9F%AC-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0)
  - 열려있는 포트 확인하는 부분 까지만 유용했다.
- [프로토콜 tcp 포트 제외 범위](https://not-to-be-reset.tistory.com/397)
- [Many excludedportranges how to delete - hyper-v is disabled](https://superuser.com/questions/1579346/many-excludedportranges-how-to-delete-hyper-v-is-disabled)

<br>



### 증상

zookeeper 를 2181 포트로 해서 docker-compose 로 구동할 때 맥북에서는 잘 되던 것이 아래와 같은 에러를 내면서 잘 실행이 안된다.

```bash
$ docker-compose up -d
[+] Running 3/4
 - Network only_kafka_default  Created                                                                                                                            0.6s
 - Container cmak              Started                                                                                                                            2.1s
 - Container kafka             Started                                                                                                                            2.5s
 - Container zk                Starting                                                                                                                           2.5s
Error response from daemon: Ports are not available: exposing port TCP 0.0.0.0:2181 -> 0.0.0.0:0: listen tcp 0.0.0.0:2181: bind: An attempt was made to access a socket in a way forbidden by its access permissions.
```

<br>



### 원인

인터넷을 찾아보니, 원인은 윈도우에서 사용자에게 제외하고 있는 포트들이 있는데 2181이 여기에 해당되는 것이었다. 쉽게 이야기하면 윈도우 이자식이 지가 쓰려고 예약해둔 포트들이다.

```bash
$ netsh interface ipv4 show excludedportrange protocol=tcp

프로토콜 tcp 포트 제외 범위

시작 포트    끝 포트
----------    --------
      1125        1224
      1688        1787
      1788        1887
      2180        2279	# 여기!!
      2869        2869
      4948        5047
      5048        5147
      5357        5357
      6788        6887
      6889        6988
     11504       11603
     11665       11764
     12096       12195
     12255       12354
     49669       49768
     49769       49868
     50000       50059     *

* - 관리 포트 제외입니다.
```



위의 결과를 자세히 살펴보면 시작포트 2180 \~ 2279 사이의 포트가 window 운영체제를 위해 예약되어 있다.

<br>



### 해결방법

해결방법은 두가지가 있다.

- 1\) Docker Desktop \> Settings \> Start Docker Desktop when you login 에 대한 체크박스를 활성화
  - Docker Desktop 프로그램을 윈도우 시작시 구동시키도록 하는 옵션이다.
- 2\) winnat 을 종료 후 도커 컴포즈 실행 → winnat 다시 start



#### 2\) netsh 명령어로 excluded 명령어 실행

참고자료

- [프로토콜 tcp 포트 제외 범위](https://not-to-be-reset.tistory.com/397)
- [Many excludedportranges how to delete - hyper-v is disabled](https://superuser.com/questions/1579346/many-excludedportranges-how-to-delete-hyper-v-is-disabled)

<br>



명령어

- 반드시... Windows Terminal 을 관리자 권한으로 실행해야 한다.



```bash
$ net stop winnat
Windows NAT Driver 서비스를 잘 멈추었습니다.

$ docker-compose up -d

# ...


$ net start winnat
Windows NAT Driver 서비스가 잘 시작되었습니다.
```





