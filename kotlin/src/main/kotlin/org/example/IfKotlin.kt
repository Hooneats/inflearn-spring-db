package org.example

/**
 * TODO :
 *  Statement : 프로그램의 문장, 하나의 값으로 도출되지 않거나 도출된다. (Expression 을 포괄하는 범위)
 *          ㄴ 자바의 if-else 문
 *     |
 *  Expression : 프로그램의 문장, 하나의 값으로 도출된다.
 *          ㄴ 코틀린의 if-else 문 , 따라서 if-else 문 전체를 리턴할 수 있다.
 */
class IfKotlin {
}

// TODO : 자바와 다르게 void , 코틀린의 Unit 을 생랼해도 된다.
fun validateScoreIsNotNegative(score: Int) {
//    if(score < 0 && score > 100)
    if (score !in 0..100) {
        throw IllegalAccessException("${score} 는 0보다 작거나 100보다 클 수 없습니다.")
    }
}
// TODO : Statement 로 구현시
fun getPassOrFail0(score: Int): String {
    if (score >= 50) {
        return "P"
    } else {
        return "F"
    }
}
// TODO : Expression 으로 구현시
fun getPassOrFail(score: Int): String {
    return if (score >= 50) {
        "P"
    } else {
        "F"
    }
}

// TODO : 자바의 switch case
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
        is String -> obj.startsWith("A") // 스마트 케스트 사용
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
