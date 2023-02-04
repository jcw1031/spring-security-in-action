# Hello! Spring Security

> Spring Security의 기본 구성 애플리케이션이 어떻게 동작하는지 확인하고
> 이 기본 구성에 속한 요소와 그 목적을 알아보자.

### 가장 간단한 예제
`com.spring.securityinaction.chapter2` 경로의 `HelloController`는
하나의 엔드포인트를 갖는 매우 간단한 예제 `Controller`이다.

'http://localhost:8080/hello' 호출 시, 상태 코드가 `401` Unauthorized이다.
인증을 위한 올바른 자격 증명을 제공하지 않았기 때문이다. -> 기본적으로 스프링 시큐리티는
기본 사용자 이름(user)과 제공된 암호(Application을 실행하면 임의의 security password가 주어진다.)를 사용할 것으로 예상한다.

<br/>

#### 올바른 자격 증명을 지정하여 호출
```text
curl -u user:[비밀번호] http://localhost:8080/hello
```
이렇게 올바른 자격 증명을 보내면 응답이 잘 온다.

