package org.example

import java.lang.IllegalArgumentException

class NullPlatformKotlin {
}

fun main() {

}
// TODO : Safe Call (안전한 호출) -> null 이 가능한 변수에 대해 한번더 안전한 장치
fun safeCall() {
    var str: String? = "ABC" // ? 를 붙였기에 null 가능
//    str.length // 불가능
    str?.length // 가능
    // null 이 아니면 뒤에 메서드 실행 , null 이면 null 리턴
}

// TODO : Elvis 연산자 -> 앞의 연산 결과가 null 이면 뒤의 값을 사용(null 이면 default 값 설정과 같다)
fun elvis() {
    var str: String? = "ABC"
    str?.length ?: 0 // null 이면 0 이되고 null 이 아니면 length 실행
}

// TODO : null O -> null X
fun startsWithA1(str: String?): Boolean {
//    if (str == null) {
//        throw IllegalArgumentException("null 이 들어왔습니다.")
//    }
//    return str.startsWith("A")

    return str?.startsWith("A") ?: throw IllegalArgumentException("null 이 들어왔씁니다.")
}
// TODO : null O -> null O
fun startsWithA2(str: String?): Boolean? {
//    if (str == null) {
//        return null
//    }
//    return str.startsWith("A")

    return str?.startsWith("A")
}
// TODO : null O -> null X
fun startsWithA3(str: String?): Boolean {
//    if (str == null) {
//        return false
//    }
//    return str.startsWith("A")

    // 다음과 같은 early return 도 가능
    str ?: return false
    return str.startsWith("A")
}

// TODO : 무조건 null 이 아닌경우
fun startsWithA4(str: String?): Boolean {
    // str 은 절대 null 이 아니라는 뜻
    return str!!.startsWith("A")
}

/**
 * TODO : 플랫폼 타입(null 인지 아닌지 모르는 타입) 은 유의하자
 *  코틀린에서 자바 객체나 자바 코드를 가져다 쓸때
 *  javax 또는 android 또는 jetbrains 의 @NotNull 또는 @Nullable 을 인식해서 사용할 수 있다.
 */
