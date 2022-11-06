package org.example.basic

fun main() {
    val dataClass1 = DataClass("홍길동1", 10)
    val dataClass2 = DataClass("홍길동2", 20)
    println(dataClass1 == dataClass2) // equals
    println(dataClass1) // toString
    println(dataClass2) // toString
}

data class DataClass(
    val name: String,
    val age: Int
)
/**
 * TODO : 코틀린의 data class 는 자동으로
 *  equals & hashcode , toString 을 만들어 준다.
 *      ㄴ 이와 비슷한 것으로 자바에서는 JDK16 부터 record class 가 생겼다.
 */
