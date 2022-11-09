package org.example.dsl.code.function;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class LambdaUtils {

    public static <T> Optional<T> optional(T target) {
        return Optional.ofNullable(target);
    }

    public static <T> Stream<T> stream(Collection<T> targets) {
        return targets.stream();
    }

    public static <T, R> R function(T resource , Function<T, R> function) {
        return function.apply(resource);
    }

    public static <T> Boolean predicate(T value, Predicate<T> predicate) {
        return predicate.test(value);
    }

    public static <T> void consumer(T resource, Consumer<T> consumer) {
        consumer.accept(resource);
    }

    public static <R> R supplier(Supplier<R> supplier) {
        return supplier.get();
    }
}
