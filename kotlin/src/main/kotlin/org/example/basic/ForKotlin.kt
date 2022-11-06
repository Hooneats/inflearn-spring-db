package org.example.basic

class ForKotlin {
}
// TODO : in/!in - 컬렉션의 범위에 포함되어있다. 포함되어 있지 않다.
// TODO : a..b - a부터 b까지 범위 객체를 생성한다.
//                ㄴ .. 은 IntRange 를 만들고 IntRange 는 IntProgression(등차수) 을 만든다
fun main() {
    val numbers = listOf(1L, 2L, 3L)
    for (number in numbers) {
        println(number)
    }

    for (i in 1..3) {
        println(i)
    }

    for (i in 3 downTo 1) {
        println(i)
    }

    for (i in 1..5 step 2) {
        println(i)
    }

    var i = 1
    while (i < 3) {
        println(i)
        i++
    }
}
