package org.example.code;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ObjectTest {

    List<TestObj> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
        TestObj testObj1 = new TestObj("1번", 10);
        TestObj testObj2 = new TestObj("2번", 20);

        list.add(testObj1);
        list.add(testObj2);
    }

    @DisplayName("객체의 생성 확인")
    @Test
    void createTestObj() {
        TestObj testObj1 = list.get(0);
        TestObj testObj2 = list.get(1);

        Assertions.assertThat(testObj1).isNotNull();
        Assertions.assertThat(testObj2).isNotNull();
    }

    @DisplayName("객체의 속성 값이 정확하게 반영됨")
    @Test
    void getTestObjFieldsValue() {
        TestObj testObj1 = list.get(0);

        Assertions.assertThat(testObj1.getName()).isEqualTo("1번");
        Assertions.assertThat(testObj1.getAge()).isEqualTo(10);
    }

    @DisplayName("객체의 속성 값 변경")
    @Test
    void setTestObjFieldsValue() {
        TestObj testObj1 = list.get(0);
        Assertions.assertThat(testObj1.getName()).isEqualTo("1번");
        Assertions.assertThat(testObj1.getAge()).isEqualTo(10);

        testObj1.setName("변경된 1번");
        testObj1.setAge(30);

        Assertions.assertThat(testObj1.getName()).isEqualTo("변경된 1번");
        Assertions.assertThat(testObj1.getAge()).isEqualTo(30);
    }

    // 예시로 테스트 파일내에 이너클래스로 클래스 작성
    static class TestObj {

        private String name;
        private int age;

        TestObj(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        int getAge() {
            return age;
        }

        void setAge(int age) {
            this.age = age;
        }
    }
}
