# Java WAS

2024 우아한 테크캠프 프로젝트 WAS

## 🖥️ 배포 주소

- [추가 예정입니다!](http://localhost:8080/)

## 📚 구현 설명

### ⚙️ 프로젝트 동작 방식
![img.png](img.png)

- `Main` 클래스에서 원하는 포트를 갖는 `ServerSocket`을 생성합니다.
- `ServerSocket`이 생성되면서 구현된 모든 `EndPointHandler`를 핸들링해 `EndPoint`를 생성 및 저장합니다.
  - 예시 이미지에서는 `8080` 포트를 사용합니다. 
- HTTP 요청이 들어오면 `ServerSocket`은 요청에 대한 `ConnectionRunner`를 스레드로 실행합니다.
- `ConnectionRunner`는 HTTP 요청을 `HttpRequest` 로 파싱합니다.
- 파싱한 `HttpRequest`에 기반해서 `EndPoint`를 찾아 `HttpResponse`를 생성합니다.
  - `EndPoint`가 존재하지 않거나 오류가 발생한 경우 이에 맞는 예외를 `HttpResponse`로 생성합니다.
- 생성된 `HttpResponse`에 기반해 HTTP 응답을 반환합니다.

### 🔍 `EndPoint` 살펴보기
```java

public class EndPoint {
    private final String path;
    private final Function<String, byte[]> function;
    private final StatusCode statusCode;
    private String contentType;
    private String redirectUri;
}

```

- `EndPoint` 객체는 응답 Path, 요청에 대한 동작인 `Function` 인터페이스, 응답 상태 코드, 컨텐츠 타입, 리다이렉트 URI를 가지고 있습니다.
- `Function` 인터페이스는 `String` 타입의 요청을 받아 `byte[]` 타입의 응답을 반환합니다.
  - GET 요청의 경우 query를 통한 인자 전달이 가능해 이를 사용하고자 `Function<String, byte[]`를 활용했습니다. 
- 예를 들어 `/api/v1/hello` 라는 GET 요청에 Json 형식으로 안녕하세요라는 데이터를 반환한다면, 아래와 같이 `EndPoint`를 생성할 수 있습니다.
```java
EndPoint endPoint = new EndPoint("/api/v1/hello", ignore -> "{\"message\": \"안녕하세요\"}".getBytes(), StatusCode.OK, ContentType.APPLICATION_JSON);
```
- 추후 `EndPoint`를 추상화 해 `GetEndPoint`, `PostEndPoint` 등으로 세분화 할 예정입니다.
