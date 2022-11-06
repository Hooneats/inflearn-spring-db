package org.example.basic

import org.example.code.Person

class TypeKotlin {
}

fun main() {
    // TODO : 코틀린에서 Primitive 타입 변환은 to~() 메서드를 이용한다.
    val number1 = 3 // int
//    val  number2: Long = number1 // error
    val number2: Long = number1.toLong() // 명시적으로 타입변환 해야함

    val number3: Int? = 3
    val number4: Long = number3?.toLong() ?: 0L

    val  number5 = 5
    val number6 = 10
    val result = number5 / number6.toDouble()
    println(result)

    printAgeIfPersonNullable(Person("김명훈"))
}

// TODO : 코틀린에서는 Object 대신 Any 를 사용한다.
fun printAgeIfPerson(obj: Any) {
    // TODO : 코틀린에서는 instanceof 대신 is 를 사용한다.
    // !is 도 사용 가능한데 이는 Person 타입이 아니다 이다.
    if (obj is Person) {
        // TODO : 코틀린에서 참조 타입 변환은 as 를 사용한다.
        val person = obj as Person // 자바의 (Person) obj 와 같다
        println(person.name)
        // TODO : 코틀린에서 as Person 변환은 생랼될 수 있다. -> 스마트캐스트
        println(obj.name)
    }
}

fun printAgeIfPersonNullable(obj: Any?) {
    // TODO : as? 는 obj 가 null 이면 null 리턴 아니면 Person 으로 변환
    val person = obj as? Person
    println(person?.name)

    println("사람의 이름은 '${person?.name}' 입니다.")

    val str = "문자열입니다"
    println("""
        굉장히 긴 문자열로 자유롭게
        줄변환을 한 문자열을
        넣으면 된다.
        
        문자열의 특정 문자는 배열처럼 [] 를 사용한다.
        ㄴ> "문자열입니다" 의 두번째 [2] 사용 : ${str[2]} / get(2) 이렇게도 가능 :  ${str.get(2)}
         ㄴ> nullable 인 문자는 get 을 사용 ${person?.name?.get(2)}
    """.trimIndent())
}

/**
 * TODO :
 *  Any - 자바의 Object 처럼 최상위 타입
 *  Unit - 자바의 void 와 동일하다
 *  Nothing - 함수가 정상적으로 끝나지 않았다는 사실을 표현
 *      ㄴ ex) 무조건 예외를 반환하는 함수 또는 무한 루프 함수 등
 */
