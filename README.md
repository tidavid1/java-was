# Java WAS

2024 우아한 테크캠프 프로젝트 WAS

## 🖥️ 배포 주소

- [AWS EC2 - 제 IP만 접속이 가능합니다](http://13.124.250.27:8080/)

## 📚 구현 설명

### ⚙️ 프로젝트 동작 방식

![img.png](img.png)

- `Main` 클래스에서 원하는 포트를 갖는 `ServerSocket`을 생성합니다.
- `ServerSocket`이 생성되면서 구현된 모든 `EndPointHandler`를 핸들링해 `EndPoint`를 생성 및 저장합니다.
    - 예시 이미지에서는 `8080` 포트를 사용합니다.
- HTTP 요청이 들어오면 `ServerSocket`은 요청에 대한 `ConnectionRunner`를 스레드로 실행합니다.
    - `ConnectionRunner`는 HTTP 요청 `InputStream`을 `HttpRequestHandler`에 위임합니다.
    - `ConnectionHandler`는 `HttpRequestParser`로 `HttpServletRequest` 객체를 생성합니다.
    - `ConnectionHandler`는 응답을 담기 위한 `HttpServletResponse` 객체를 생성한 후 `HttpServletRequest`
      객체와 `HttpServletResponse`객체를 `FilterChain`에 전달합니다.
    - `FilterChain`을 통과하며 `HttpServletResponse` 객체에 응답을 담습니다.
        - `FilterChain`의 필터 중 `EndPointProviderFilter`는 `HttpServletRequest` 객체의 Path를 통해 `EndPoint`
          를 찾아 결과값을 `HttpServletResponse` 객체에 응답을 담습니다.
        - 동적인 HTML 파일을 응답하기 위해 특정 `EndPoint`는 `HTMLConvertor` 를 활용해 동적으로 값을 전달합니다.
- `ConnectionRunner`는 완성된 `HttpServletResponse` 객체에 `OutputStream`을 위임해 응답을 전송합니다.

### 🔍 `Filter`, `FilterChain` 살펴보기

- 작성중

### 🔍 `EndPoint` 살펴보기

```java

import java.util.function.BiConsumer;

public class EndPoint {

    private final String path;
    private final BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer;
}

```

- `EndPoint` 객체는 응답 Path, 요청에 대한 동작인 `BiConsumer` 함수형 인터페이스를 가지고 있습니다.
- `BiConsumer` 인터페이스는 `HttpServletRequest` 객체와 `HttpServletResponse` 객체를 인자로 받아 동작합니다.
    - `HttpServletRequest`에 담긴 정보를 통해 `HttpServletResponse`에 동적으로 응답을 담습니다.

### 🔍 `HTMLConvertor` 살펴보기

- 작성중
