# 자바 마치며

## 1. 반복문
>for와 while 등의 반복문이 어떤 상황에서 어떻게 쓰이는지

## 2. 배열
- 한 번 크기를 결정하면 크기를 바꿀 수 없다는 것이 가장 큰 특징이다.
- 배열의 처리는 순서적이다. 
- 중간에 데이터 삽입이 불가능한 구조이다.
- 배열의 배열
- 배열의 복사 (배열을 늘려서 또 다른 배열을 만드는 등)
>> - Shallow copy / Deep copy

## 3. 객체지향
### 3.1. 클래스를 만들 때는 서로 관련성 있는 것끼리 묶는다.
> 안에 불필요한 연산이 들어가서는 안 된다.
> DTO 등의 클래스를 만들 때는 필드의 접근제한자는 private과 같은 접근제한자를 명시한다. 이러한 규약을 지켜야 함. 필드를 만들 때 대개 90% 이상은 private이다. 
> 물론.. 변수명의 시작은 영소문자이다.
> 변수의 성격이 비슷하다고 할지라도 한 줄에 하나만의 변수를 선언한다.
```java
int a, b, c; //이거 하지 말고
int a;
int b;
int c;
```
### 3.2. 하나의 메서드는 하나의 기능만을 담당하도록 한다.
> 변수와 마찬가지로 이름은 영소문자로 시작하는 것이 원칙이다.
> 경우에 따라서는 메서드나 필드는 앞에 static키워드가 들어갈 수 있는데, 남용되거나 치명적인 오류가 발생할 수 있으므로 사용을 되도록이면 지양하도록 한다.
> static으로 만드는 변수는 final과 결합하여 static final 변수(상수)를 만드는 경우에 사용한다.

### 3.3. 생성자
> Default Constructor는 생성자를 하나라도 개발자가 명시한 경우에는 만들어지지 않는다는 것에 유의한다.
> 같은 클래스 내 다른 생성자를 부르기 위해서는 this(매개변수타입과개수)

## 4. API
### 4.1. String
- 다음과 같은 연산은 지양
```java
String a;
a = "가";
a += "나";
a += "다";
```
### 4.2. StringBuffer / StringBuilder
### 4.3. Wrapper 클래스
- Integer, Double 등...은 기본자료형인 int, double과 다르게 null값을 가질 수 있다. (객체니까!)
### 4.4. Calendar
### 4.5. SimpleDateFormat
### 4.6. NumberFormat

## 5. Class 클래스 (Reflection)
- Spring에서도 newInstance()메서드를 호출하여 객체를 생성해야 한다. (with MVC Pattern)

## 6. 상속
> Spring 등 프레임워크에서 이미 개발된 클래스를 상속받아서 작업해야 하는 경우가 많다.
> 상속은 단일상속만 가능
> super 키워드 기억!

### 6.1. 업캐스팅
### 6.2. 다운캐스팅
- 인터페이스를 이용하여 다운캐스팅 과정을 거칠 필요가 없도록 구현하므로 인터페이스를 사용하는 방법에 대해 익혀 둔다.

## 7. 예외처리 (try-catch)
> 예외처리는 무!조!건! 해야 한다.
1. 문자 => 숫자 변환
2. 클라이언트에 데이터를 넘겨 받을 때 등...
> 예외처리를 할 경우 오류 메시지를 출력하여 왜 문제가 생기는지를 파악하는 것도 중요하다.
> Exception보다는 상황에 맞는 예외인 NumberFormatException, IOException, InputMisMatchException 등을 먼저 잡는 것이 기본이다.
> 예외를 잡아버리면 호출하는 쪽에서 예외가 잡히지 않는다. 따라서 이럴 때는 예외를 명시하여 throw하도록 한다.
> catch부분에서 오류를 받았을 때 처리하는 부분을 명시하므로 코드가 훨씬 간결해진다.

## 8. 컬렉션
- 클래스명 앞에 Tree가 들어가면 정렬과 관련된 클래스구나! 생각

### 8.1. ArrayList / Vector (가변적 배열)
- 자주 사용되는 메서드 인지
- 수시로 삭제되거나 삽입되는 경우에는 LinkedList를 사용하는 것이 좀 더 처리가 빠르다. LinkedList는 용량을 좀 더 많이 차지하므로 데이터의 변동이 매우 심할 때 사용한다.
### 8.2. HashMap (맵)
- 저장: put(Key, Value)
- 인출: get(Key)
### 8.3. Set
- 중복적인 데이터를 피하고자 할 때...
### 8.4. Comparator 
- 정렬 기준값을 알려주기 위해...
- 이왕이면 Collections에 구현된 정렬 메서드 사용하는 것을 권장

## 9. Thread
- 이론적인 부분
- 동시성의 의미 
>StringBuilder(동시성 x) / StringBuffer(동시성 o)
>ArrayList(동시성 x) / Vector(동시성 o)
>Map (동시성 x) / Table(동시성 o)
>BlockingQueue

## 10. I/O 스트림
- 파일로 저장하고 불러오는 구문
>> 성적처리 예제 참조할 것

## 11. 직렬화 (Serialization)
- 객체를 저장하거나 불러올 필요가 있으면 implemenets Serializable 명시
- ObjectInputStream, ObjectOutputStream

## 12. JDBC
- DB와 연동

## 13. 람다식 (JDK 7)

## 14. 스트림 (JDK 8)
