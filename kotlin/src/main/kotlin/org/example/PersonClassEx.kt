package org.example

import java.lang.IllegalArgumentException

/**
 * TODO :
 *  - 코틀린에서는 기본 접근제어자가 public 이다.(생략가능)
 *  - 코틀린에서는 val 과 var 키워드가 있다면 자동으로 setter, getter 를 만들어 준다.
 *  - 코틀린에서의 getter 는 .필드명 으로 사용가능
 *  - 코틀린에서의 setter 는 .필드명 = 값 으로 사용가능
 */
class PersonClassExV1 {
    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }

    val name: String   // val 변수이기에 setter 를 만들지 않는다.
    var age: Int
}
// TODO : - 추가적으로 constructor 라는 지시어는 생략가능하다.
class PersonClassExV2 (name: String, age: Int) {
    val name = name   // val 변수이기에 setter 를 만들지 않는다.
    var age = age
}
// TODO : 생성자에서 바로 필드를 만들 수 있다.
class PersonClassExV3 (
    val name: String, var age: Int
    ) {
}
// 이때 바디는 아무것도 없기에 생략가능하다.
class PersonClassExV4 (
    val name: String, var age: Int
)
// TODO : 만약 생성자안에 로직이(검증로직등) 들어가야 한다면 코틀린에서는 init 을 사용한다.
class PersonClassExV5(
    val name: String, var age: Int
) {
    init { // init 블록은 초기화가 호출되는 시점에 호출된다.
        if (age < 0) {
            IllegalArgumentException("나이는 0 이하일 수 없습니다.")
        }
    }
}

// TODO : 만약 코틀린에서 생성자 오버로딩을 사용하고 싶다면(여러개의 생성자를 만들고 싶다면) constructor 키워드를 사용
class PersonClassExV6(
    val name: String, var age: Int // 주 생성자 , 반드시 존재해야함, 파라미터가 하나도 없는경우 생략가능
) {
    init { // init 블록은 초기화가 호출되는 시점에 호출된다.
        if (age < 0) {
            IllegalArgumentException("나이는 0 이하일 수 없습니다.")
        }
        println("초기화 블록, 주 생성자 호출시 호출됩니다.")
    }
    constructor(name: String) : this(name, 1) // 부 생성자, 있을 수도 있고, 없을 수도 있다. TODO :최종적으로 주 생성자를 호출해야한다. this()
    constructor() : this("홍길동") { // TODO : 부 생성자는 body 를 가질 수 있다.
        println("두 번째, 부 생성자 입니다. / 현재 로직으로는 첫 번째 부 생성자를 호출합니다.")
    }
}
fun main() {
    // TODO : 생성자 호출 순서는 자바와 다르게 역순이다.
    val p = PersonClassExV6() // 두 번째 부 생성자 호출
    // 결과는 주 생성자 init -> 첫 부 생성자 -> 두 번째 부 생성자
    println(p.name + p.age)
}
// TODO : 그러나 코틀린에서는 기본적으로 부 생성자 보다는 default parameter 를 권장한다.
class PersonClassExV7 (
    val name: String = "김명훈", var age: Int = 1
) {
    init { // init 블록은 초기화가 호출되는 시점에 호출된다.
        if (age < 0) {
            IllegalArgumentException("나이는 0 이하일 수 없습니다.")
        }
    }
}

// TODO : 코틀린에서는 다음과 같이 인스턴스 함수를 작성할 수 있다.
class PersonClassExV8 (
    val name: String = "김명훈", var age: Int = 1
) {
    init {
        if (age < 0) {
            IllegalArgumentException("나이는 0 이하일 수 없습니다.")
        }
    }
// TODO : 함수처럼 만드는 방법
    fun isAdultV1(): Boolean {
        return this.age >= 20
    }
// TODO : custom getter 를 활용해 변수처럼 보이는 방법
    val isAdultV2: Boolean
        get() = this.age >= 20
    val isAdultV3: Boolean
        get() {
            return this.age >= 20
        }
    // TODO : 따라서 객체의 속성이면 속성처럼 사용하는 custom getter 를 사용하는것이 좋다.
}
// TODO : custom getter 활용
class PersonClassExV9 (
    name: String, var age: Int = 1
) {
    // 생성시 넣은 name 필드를 getter 를 사용시 대문자로 나오게
    val name: String = name
        get() = field.uppercase() // name.uppercase() 를 사용시 무한루프 발생(name 호출로인해) 때문에 자기 자신을 가리키는 backing filed 인 filed 를 사용
}
// TODO : 그러나 backing filed 를 사용하기 보다는 아래처럼 this 를 사용 하는 것이 좋아보인다.
class PersonClassExV10 (
    val name: String, var age: Int = 1
) {
    val upperName: String
        get() = this.name.uppercase()
}
// TODO : Custom Setter
class PersonClassExV11 (
    name: String, var age: Int = 1
) {
    var name: String = name
        set(value) {
            field = value.uppercase()
        }
    // TODO 하지만 setter 는 지양해야하므로 custom setter 는 거의 쓸일 없다.
}
