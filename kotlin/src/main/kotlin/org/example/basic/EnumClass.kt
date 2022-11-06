package org.example.basic

/**
 * @JavaEnumClass
 */
enum class EnumClass(
    private val code: String
) {
    KOREA("한국"),
    AMERICA("미국");
}

fun handleCountry(country: EnumClass) {
    when (country) {
        EnumClass.KOREA -> TODO()
        EnumClass.AMERICA -> TODO()
        // ENUM 특성상 else 를 작성할 필요 없다.
    }
}
