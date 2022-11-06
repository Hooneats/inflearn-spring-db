package org.example.basic

class InnerClassKotlin {
}

// TODO : 권장되는 inner 클래스 만드는 방식으로 static inner 클래스가 생성된다.
class House(
    private val address: String,
    private val livingRoom: LivingRoom
) {
    class LivingRoom(
        private val area: Double
    )
}

// TODO : 권장되지 않는 inner 클래스 만드는 방식으로 static 이 아닌 inner 클래스가 생성된다.
class HouseV2(
    private val address: String,
    private val livingRoom: LivingRoom
) {
    inner class LivingRoom(
        private val area: Double
    ) {
        // TODO : 바깥 클래스 참조시에는 this@ 를 사용한다.
        val address: String
            get() = this@HouseV2.address
    }
}

