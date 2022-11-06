package org.example.basic

class ClassExtendsEx {
}

/**
 * @JavaAnimal
 */
abstract class Animal(
    protected val species: String,
    protected open val legCount: Int // 코틀린은 자바와 다르게 속성을 오버라이딩(게터나셋터) 할때는 open 을 붙여줘야 한다.
){
    abstract fun move()
}
// TODO : 코틀린에서 상속은 '한 칸 뛰고 : ' 사용한다. - 이는 인터페이스도 마찬가지이다.
/**
 * @JavaCat
 */
class Cat(
    species: String
) : Animal(species, 4) { // Animal(species, 4) 는 자바에서 supper(species, 4) 와 같다.
    override fun move() {
        println("고양이가 사뿐 사뿐 걸어")
    }
}
/**
 * @JavaPeguin
 */
class Penguin(
    species: String
) : Animal(species, 2), Swimable, Flyable {
    private val wingCount: Int = 2
    override fun move() {
        println("팽귄이 움직입니다.")
    }
    override val legCount: Int
        get() = super.legCount + this.wingCount

    override fun act() {
        super<Swimable>.act()
        super<Flyable>.act()
    }
}
/**
 * @JavaSwimable
 */
interface Swimable {
    fun act(){
        println("어푸 어푸")
    }
}
/**
 * @JavaFlyable
 */
interface Flyable {
    // default 를 쓰지 않아도 된다.
    fun act(){
        println("파닥 파닥")
    }
}

fun main() {
    Derived(300)
}
// TODO : 상위 클래스를 설계할때 생성자 또는 초기화 블록에 사용되는 프로퍼티에는 open 을 피해야 한다.
open class Base(
    open val number: Int = 3 // 상속을 막는 final 이 기본으로 들어가 있기에 open 을 붙여줘야 한다.
) {
    init {
        println("Base class")
        // TODO : 즉 여기서 number 사용시 기본 3 또는 구현체생성시 넣은 300 이 아닌 0 이 출력된다 원치않는 값이므로 초기화 블록에서 사용시 주의
        println(number)
    }
}
class Derived(
    override val number: Int
) : Base(number) {
    init {
        println("Derived class")
        println(number)
    }
}

/**
 * TODO :
 *      final - override 할 수 없게 한다. default 로 보이지 않게 존재한다.
 *      open - override 를 열어준다. override 를 해도되고 안해도 된다.
 *      abstract - 반드시 override 를 해줘야 한다.
 *      override - 상위 타입을 오버라이 하고 있다.
 */

/**
 *  TODO : 코틀린에서의 접근제어자
 *          Java                     |           Kotlin
 *    public - 모든 곳에서 접근 가능          public - 모든 곳에서 접근 가능
 *    protected - 같은 패키지 또는 하위       protected - 선언된 클래스 또는 하위
 *                  클래스에서 접근 가능                      클래스에서 접근 가능 (패키지란 개념이 없음! / 클래스에서만 사용가능)
 *    default - 같은 패키지에서만 접근 가능     internal - 같은 모듈에서만 접근 가능
 *    private - 선언된 클래스 내에서만 가능     private - 선언된 파일 및 클래스 내에서만 가능
 */

// TODO : 코틀린의 클래스는 public constructor 가 생략되어있다. 즉 클래스의 접근제어자는 constructor 앞에 온다.

// TODO : Java 에서 유틸성 코드를 만들때 abstract class + private constructor 를 인스턴스화도 안되고 상속도 안되게 막으면 좋다 이는 코틀린도 같다.

// 아래와 같이 속성 접근제어자를 getter 와 setter 나눠 사용가능하다.
class Car(
    internal val name: String,
    private var owner: String,
    _price: Int
) {
    var _price = _price
        private set
}

/**
 * TODO : 자바와 코틀린의 접근제어자가 다르기에 생기는 점
 *  1) internal 은 바이트 코드 상 public 이 된다. 때문에 java 코드에서는 kotlin 모듈의 internal 코드를 가져올 수 있다.
 *  2) kotlin 의 protected 와 java 의 protected 는 다르다. 때문에 java 코드에서는 같은 패키지의 Kotlin Protected 멤버에 접근할 수 있다.
 */
