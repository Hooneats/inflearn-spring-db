package org.example.dsl.code.function;

import static org.junit.jupiter.api.Assertions.*;

import spock.lang.Specification;

class ActionTest extends Specification {

    List<TestObj> list = new ArrayList<>()

    def "setup"() {
        list.add(new TestObj("1번", 10))
        list.add(new TestObj("2번", 20))
    }

    def "cleanup"() {
        list.clear()
    }

    def "Action 의 Map 이 정상작동한다."() {
        given:
        def testObj01 = list[0]

        when:
        def findAge = Action.start(testObj01)
                .map(testObj -> testObj.getAge())
                .end()

        then:
        findAge == 10
    }

    static class TestObj {
        private String name
        private int age

        TestObj(String name, int age) {
            this.name = name
            this.age = age
        }

        String getName() {
            return name
        }

        void setName(String name) {
            this.name = name
        }

        int getAge() {
            return age
        }

        void setAge(int age) {
            this.age = age
        }
    }
}
