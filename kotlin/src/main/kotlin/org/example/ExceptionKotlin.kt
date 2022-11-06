package org.example

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.IllegalArgumentException

class ExceptionKotlin {
}

fun main() {
    readFile()
    readFileV2()
}

fun parseIntOrThrowV0(str: String): Int {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("주어진 ${str} 은 숫자가 아닙니다.")
    }
}
fun parseIntOrThrowV1(str: String): Int? {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        return null
    }
}
// TODO : 코틀린에서 try-catch 또한 Expression 으로 간주한다.
fun parseIntOrThrowV2(str: String): Int? {
    return try {
        str.toInt()
    } catch (e: NumberFormatException) {
        null
    }
}
// TODO : 코틀린에서는 Checked-Exception 과 UnChecked-Exception 을 구분하지 않고 모두 UnChecked-Exception 으로 간준한다.
fun readFile() {
    val file: File = File("./kotlin/src/main/kotlin/org/example")
    val targetFile: File = File(file.absolutePath + "/a.txt")
    val reader: BufferedReader = BufferedReader(FileReader(targetFile))
    println(reader.readLine())
    reader.close()
}
// TODO : 코틀린에서는 try-with-resources 가 없다. --> .use() 라는 inline 확장 함수를 사용하면 된다.
fun readFileV2() {
    val file: File = File("./kotlin/src/main/kotlin/org/example")
    val targetFile: File = File(file.absolutePath + "/a.txt")
    BufferedReader(FileReader(targetFile)).use {
        reader -> println(reader.readLine())
    }
}
