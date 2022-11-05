package org.example

class IfKotlin {
}

fun validateScoreIsNotNegative(score: Int) {
//    if(score < 0 && score > 100)
    if (score !in 0..100) {
        throw IllegalAccessException("${score} 는 0보다 작거나 100보다 클 수 없습니다.")
    }
}

fun getPassOrFail(score: Int): String {
    return if (score >= 50) {
        "P"
    } else {
        "F"
    }
}

// 자바의 switch case
fun getGradeWithSwitch(score: Int): String {
    return when (score / 10) {
        // 아래의 in 처럼 들어갈 수 있는 키워드는 in, is
        in 90..99 -> "A"
        in 80..89 -> "n"
        in 70..79 -> "C"
        else -> "D"
    }
}
fun startsWithA(obj: Any): Boolean {
    return when (obj) {
        is String -> obj.startsWith("A")
        else -> false
    }
}
fun judgeNumber(number: Int) {
    when (number) {
        1, 0, -1 -> println("어디서 많이 본 숫자입니다.")
        else -> println("1,0,-1 이 아닙니다.")
    }
}
fun judgeNumber2(number: Int) {
    when {
        number == 0 -> println("주어진 숫자는 0 입니다.")
        number % 2 == 0 -> println("주어진 숫자는 짝수 입니다.")
        else -> println("주어진 숫자는 홀수 입니다.")
    }
}