package org.example;

import org.example.code.Person;

public class Type {

    public static void main(String[] args) {

    }

    public static void printAgeIfPerson(Object obj) {
        if (obj instanceof Person) {
            Person person = (Person) obj;
            System.out.println("person.getName() = " + person.getName());
        }
    }
}
