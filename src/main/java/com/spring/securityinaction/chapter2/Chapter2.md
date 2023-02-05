# 2. Hello! Spring Security

> Spring Security의 기본 구성 애플리케이션이 어떻게 동작하는지 확인하고
> 이 기본 구성에 속한 요소와 그 목적을 알아보자.

## 01. 가장 간단한 예제
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

## 02. 기본 구성이란?
이 절에서는 전체 아키텍처에서 인증과 권한 부여를 참여하는 데 참여하는 주 구성 요소에 대해 논의한다.
이러한 사전 구성된 요소를 애플리케이션의 필요에 맞게 재정의해야 하기 때문이다.

### 기본적인 구성

아래는 스프링 시큐리티 아키텍처의 주 구성 요소와 이들 간의 관계를 나타낸 그림이다.

![구성 요소와 관계](https://user-images.githubusercontent.com/69714701/216767829-e2c3adc6-7ab4-48d2-9f90-a1caa23d18a4.jpeg)

- 인증 필터는 인증 요청을 인증 관리자에 위임하고 응답을 바탕으로 보안 컨텍스트를 구성한다.
- 인증 관리자는 인증 공급자를 이용해 인증을 처리한다.
- 인증 공급자는 인증 논리를 구현한다.
- 인증 공급자는 사용자 관리 책임을 구현하는 사요자 세부 정보 서비스를 인증 논리에 이용한다.
- 인증 공급자는 암호 관리를 구현하는 암호 인코더를 인증 논리에 이용한다.
- 보안 컨텍스트는 인증 프로세스 후 인증 데이터를 유지한다.

##### 자동으로 구성되는 빈
- `UserDetailsService`
- `PasswordEncoder`

인증 공급자가 이러한 빈을 이요해 사용자를 찾고 암호를 확인하는 것이다.

### 인증에 필요한 자격 증명을 제공하는 방법
사용자에 관한 세부 정보는 스프링 시큐리티로 `UserDetailsService` 계약을 구현하는
객체가 관리한다. 방금 예제는 스프링 부트가 제공하는 기본 구현을 사용했던 것이다.
이 기본 구현은 개념 증명의 역할을 하고 종속성이 작동하는 것을 확인해준다.

`PasswordEncoder`는 암호를 인코딩하고, 암호가 기존 인코딩과 일치하는지 확인하는 역할을 한다.

> HTTP Basic 인증은 자격 증명의 기밀성을 보장하지 않는다.

`AuthenticationProvider`는 인증 논리를 정의하고 사용자와 암호의 관리를 위임한다.
기본 구현은 `UserDetailsService` 및 `PasswordEncoder`에 제공된 기본 구현을 이용한다.

## 03. 기본 구성 재정의
애플리케이션을 개발할 때 맞춤형 구현체를 연결하고, 애플리케이션에 맞게 보안을 적용하기 위해서는 기본 구성을 재정의할 수 있어야 한다.
여러 방법으로 재정의할 수 있지만, 이러한 유연성을 남용해서는 안 된다. 여러 옵션 중에 선택하는 방법을 배워야 한다.

### `UserDetailsService` 구성 요소 재정의
나중에 자세한 내용을 배우겠지만, 이번에는 직접 구현체를 만들지 않고 스프링 시큐리티에 있는 `InMemoryUserDetailsManager`라는 구현체를 이용한다.
> `InMemoryUserDetailsManager` 구현체는 운영 단계 애플리케이션을 위한 것은 아니며, 예제나 개념 증명용으로 좋은 구현체이다.
> 사용자만으로 충분할 때는 다른 부분을 구현하는 데 시간을 쓸 필요가 없다!

`chapter2.config` 패키지에 `UserDetailsServiceConfig` 클래스를 통해 `UserDetailsService` 구현체를 빈으로 수동 등록했다.
이 코드를 추가함으로써 애플리케이션 실행 시, 콘솔에 더이상 암호가 출력되지 않는다.

하지만 두 가지 이유로 아직 Endpoint에 접근할 수 없다.
- 사용자가 없다.
- `PasswordEncoder`가 없다

단계별로 해결한다.
1. 자격 증명(사용자 이름 및 암호)이 있는 사용자를 하나 이상 만든다.
2. 사용자를 `UserDetailsService`에서 관리하도록 추가한다.
3. `UserDetailsService`가 저장하고 관리하는 암호를 이용해, 주어진 암호를 검증하는 `PasswordEncoder` 형식의 빈을 정의한다.

1번과 2번은, `UserDetailsServiceConfig`에서 `org.springframework.security.core.userdetails.User` 클래스로 사용자를 나타내는 객체를 생성해 추가하였다.

하지만 `UserDetailsService`를 재정의하면 `PasswordEncoder`도 다시 선언해야 한다.
지금은 Endpoint를 호출해도 예외가 발생한다. 아직 스프링 시큐리티가 암호를 관리하는 방법을 모른다는 것을 인식하고 오류를 생성하는 것이다.

`PasswordEncoderConfig`에서 `NoOpPasswordEncoder`를 컨텍스트에 추가하였다.
> `NoOpPasswordEncoder` 인스턴스는 암호화나 해시를 적용하지 않고 일반 텍스트처럼 처리하기 때문에, 운영 단계에서는 사용하면 안 된다. 예제에서 암호의 해싱 알고리즘을
> 신경 쓰고 싶지 않을 때 사용하기 좋은 옵션이다.

### Endpoint 권한 부여 구성 재정의
우리는 애플리케이션의 모든 Endpoint를 보호할 필요가 없고, 보안이 필요한 Endpoint에 다른 권한 부여 규칙을 선택해야 할 수도 있다.
이러한 변경을 위해 설정이 필요하다.
> 예전에는 `WebSecurityConfigurerAdapter`를 상속받아 `configure(HttpSecurity http)` 메서드를 재정의하였지만,
> 최근에는 `SecurityFilterChain` 빈을 통해 HttpSercurity를 구성할 수 있다.

`chapter2.config` 패키지의 `SecurityConfig` 클래스가 해당 설정을 담당한다.

지금까지의 방법은 `UserDetailsService`와 `PasswordEncoder`를 빈으로 등록해 재정의하는 방법이었다.
다른 방법은 `AuthenticationManager`를 설정하는 방법도 있다.

### AuthenticationProvider 재정의
지금까지 Spring Security Architecture에서 `UserDetailsService` 및 `PasswordEncoder`의 목적과 이를 구성하는 방법을 배웠다.
(사실 두 개의 방법이 나오지만, 설명하지 않은 방법은 추천하지 않기 때문이다.)
이제 이들 구성 요소에 작업을 위임하는 `AuthenticationProvider`도 맞춤 구성 하는 방법을 배워보자. -> 인증 공급자

> `AuthenticationProvider`는 인증 논리를 구현한다. `AuthenticationManager`에서 요청을 받은 후 사용자를 찾는 작업은 `UserDetailsService`에게,
> 암호를 검증하는 작업을 `PasswordEncoder`에 위임한다.

CustomAuthenticationProvider 클래스에서 AuthenticationProvider를 재정의 하였다.

## 정리
- Spring Security 의존성을 추가하면, Spring Boot가 약간의 기본 구성을 제공한다.
- 인증과 인가를 위한 기본 구성 요소인 UserDetailsService와 PasswordEncoder, AuthenticationProvider를 구현했다.
- User 클래스로 사용자를 정의할 수 있다. 사용자는 사용자 이름, 암호, 권한을 가져야 한다. 권한은 사용자가 애플리케이션 컨텍스트에서 수행할 수 있는 작업을 지정한다.
- Spring Security는 UserDetailsService의 간단한 구현체인 InMemoryUserDetailsManager를 제공한다. UserDetailsService의 인스턴스와 같은 사용자를 추가하여
애플리케이션 메모리에서 사용자를 관리할 수 있다.
- NoOpPasswordEncoder는 PasswordEncoder의 계약을 구현하며, 암호를 일반 텍스트로 처리한다. 따라서 학습 용도로 사용하기는 적합하지만 운영 단계 애플리케이션에는 적합하지 않다.
- AuthenticationProvider 계약을 이용하여 애플리케이션의 맞춤형 인증 논리를 구현할 수 있다.