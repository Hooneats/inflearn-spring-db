package org.example.stream;

import java.util.stream.LongStream;

public class FactorialStream {

    public static void main(String[] args) {
        System.out.println(factorialStreams(3));
    }

    static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n)
            .reduce(1, (left, right) -> left * right);
    }
}
