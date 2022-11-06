package org.example.code.class_extends;

public interface JavaFlyable {

    default void act() {
        System.out.println("파닥 파닥");
    }
}
