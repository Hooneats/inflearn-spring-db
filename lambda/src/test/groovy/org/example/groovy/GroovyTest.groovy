package org.example.groovy

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import spock.lang.Shared
import spock.lang.Specification

// https://jojoldu.tistory.com/228
class GroovyTest extends Specification {

//    @Shared
    Map<String, String> map = new HashMap<>()

    def "setup"() {
        map.put("1", "일번")
        map.put("2", "이번")
        map.put("3", "삼번")
        map.put("4", "사번")
        map.put("5", "오번")
        map.put("6", "욱번")
        map.put("7", "칠번")
    }

    void "cleanup"() {
        map.clear()
    }

    def "찾기"() {
        given:
        def id = 1

        when:
        def findString = map[String.valueOf(id)]

        then:
        findString == "일번"
    }
}
