package org.example

import org.example.code.Person

class ValVarKotlin {
}

// 타입을 의무적으로 작성하지 않아도 된다.
fun main() {
    var number1 = 10L // 가변
    number1 = 5L

    val number2 = 10L // 불변

    // 타입작성
    var number3: Long
    number3 = 1_000L
    println(number3)
    // 초기화
    var number4: Long = 1_000L
    println(number4)

    // null 사용
    var number5: Long? = null

    // 인스턴스 new 없이 사용
    var Person = Person("김명훈")

    // TODO 되도록 불변으로 만드는 것이 좋다. => val 사용

    // TODO 코틀린은 Long 을 사용하지만 필요시 내부적으로 알아서 primitive 로 변환해준다.

    // TODO 코틀린은 null 을 변수에 들어갈 수 있다면 타입? 를 사용

    // TODO 코틀린은 객체의 인스턴스를 만들 때 new 를 사용하지 않는다.

    // TODO 코틀린의 === 는 동일성 (자바의 == ) 비교이다.(주소 비교)
    // TODO 코틀린의 == 는 동등성 (자바의 equals 를 간접 호출) 비교이다.(값 비교)

    // TODO : a[i] - a 에서 특정 인덱스 i 로 값을 가져온다.
    // TODO : a[i] = b  - 특정 인덱스 i 에 값을 넣을 수 있다.
}
