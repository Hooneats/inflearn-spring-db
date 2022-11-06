package org.example.basic

class LambdaKotlin(public val name: String, val age: Int) {
}

fun main() {
    repeat("Hello World")
    repeat("Hello World", 4)
    repeat("Hello World", 4, false)
    repeat("Hello World", useNewLine = false) // named argument 사용
    // TODO : named argument 는 자바의 빌더 처럼 사용가능하다.
    //  ㄴ--> 주의할 점은 자바의 코드를 가져다 쓸때 named argument 는 사용할 수 없다.
    //  ㄴ--> 그 이유는 JVM 상에서 Java 가 바이트 코드로 변환됐을 때에 파라미터 이름을 보존하고 있지 않기 때문이다.
    val lambdaKotlin = LambdaKotlin(
         age = 3,
        name = "김명훈"
    )
    println()
    // TODO : 가변인자 사용시 만약 배열을 넘긴다면 * 를 붙여준다.
    varargFun("A", "B", "C")
    val arrayStr = arrayOf("A", "B", "C", "D")
    varargFun(*arrayStr)
    println()
}

public fun maxV0(a: Int, b: Int): Int {
    return if (a > b) {
        a
    } else {
        b
    }
}
// TODO : 함수가 하나의 결과값이라면 블록대신 = 기호를 사용가능
fun maxV1(a: Int, b: Int): Int  =
    if (a > b) {
        a
    } else {
        b
    }
// TODO : 코틀린의 타입추론(어떤경우든 Int 반환)과 블록대신 '=' 을 사용했기에 반환타입을 생략가능
fun maxV2(a: Int, b: Int)  = if (a > b) {a} else {b}

fun maxV3(a: Int, b: Int)  = if (a > b) a else b

// TODO : 코틀린에서 디폴트 파라미터를 사용한다면 자바의 오버로딩을 줄일 수 있다.
fun repeat(str: String, num: Int = 3, useNewLine: Boolean = true) {
    for (i in 1..num) {
        if (useNewLine) {
            println(str)
        } else {
            print(str)
        }
    }
}

// TODO : 코틀린에서 자바의 가변인자 (String.. strs) 를 사용하는 방법은 vararg 를 사용한다.
fun varargFun(vararg strs: String) {
    for (i in strs) {
        print(i)
    }
}
