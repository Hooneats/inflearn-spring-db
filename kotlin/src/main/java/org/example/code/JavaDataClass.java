package org.example.code;

import java.util.Objects;

public class JavaDataClass {

    private final String name;
    private final int age;

    public JavaDataClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JavaDataClass that = (JavaDataClass) o;
        return age == that.age && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "JavaDataClass{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}
