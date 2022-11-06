package org.example.basic

/**
 * TODO : Sealed class , Sealed interface
 *      이넘의 개념이 상속에 갔다고 생각하면 쉽다.
 *          컴파일시에 상속관계를 enum 처럼 이미 구현한 것으로 봉인(한정) 한다.
 *          즉, 컴파일 시에 하위 클래스의 타입을 모두 기억하고, 런타임때 클래스 타입이 추가 될 수 없다.
 *      ㄴ Enum 과 다른점 :
 *          - 클래스를 상속받을 수 있다.
 *          - 하위 클래스는 멀티 인스턴스가 가능하다.
 *
 * TODO : 추가로 JAVA 에서는 JDK17 에 Sealed Class 가 추가되었다.(내 생각엔 UseCase 별로 묶는 Boundary 같은 명시적 용도로도 유용할 것 같다.)
 */
sealed class SealedClass(
    val name: String,
    val price: Long
) {}

class Avante : SealedClass("아반떼", 1_000L)
class HyundaiCar : SealedClass("현대", 2_000L)
class Grandger : SealedClass("그랜저", 3_000L)

private fun handlerCar(car: SealedClass) {
    when (car) {
        is Avante -> TODO()
        is HyundaiCar -> TODO()
        is Grandger -> TODO()
    }
}
