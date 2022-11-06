package org.example.basic

class Static_CompanionKotlin {
}

/**
 * @JavaPerson
 */
class Person private constructor (
    val name: String,
    val age: Int
) {
    // TODO : static 정의는 companion object 를 사용한다.
    companion object { // 클래스와 동행하는 유일한 object 라고 해석하면 쉽다.
        private const val MIN_AGE: Int = 1 // const val 은 컴파일시에 할당, 그냥 val 은 런타임시에 할당 때문에 진짜 상수일경우 const 사용

//        @JvmStatic // 생략가능 - 사용시 Companion 키워드 없이 접근가능 가능하도록
        fun newBody(name: String): Person {
            println("${name} , $MIN_AGE 태어났습니다.")
            return Person(name, MIN_AGE)
        }
    }
}

class PersonV2 private constructor(
    val name: String,
    val age: Int
) {
    // TODO : companion object 에는 이름을 붙일 수 있고, 인터페이스도 구현 가능하다.
    companion object Factory : Log {
        private const val MIN_AGE: Int = 1
        fun newBody(name: String): PersonV2 {
            println("${name} , $MIN_AGE 태어났습니다.")
            return PersonV2(name, MIN_AGE)
        }

        override fun log() {
            println("나는 PersonV2 클래스의 동행 객체예요.")
        }
    }
}
interface Log {
    fun log()
}

fun main() {
    // TODO : 사용 - 이름이 없는경우
    Person.newBody("홍길동1")
    Person.newBody("홍길동2")
    // TODO : 사용 - 이름이 있는경우
    PersonV2.newBody("홍길동3")
    PersonV2.newBody("홍길동4")
    PersonV2.log()

    println(SingletonClass.a)
}


// TODO : 코틀린에서 싱글톤은 object 키워드를 사용하면된다.
object SingletonClass {
    var a: Int = 1
}

// TODO : 코틀린에서 익명클래스의 사용또한 object : 타입명 을 사용한다.
interface Movable {
    fun move()
    fun fly()
}
private fun moveSomething(movable: Movable) {
    movable.move()
    movable.fly()
}
fun test() {
 moveSomething(object : Movable {
     override fun move() {
         println("move")
     }

     override fun fly() {
         println("fly")
     }
 })
}
