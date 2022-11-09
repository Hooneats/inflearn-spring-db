package org.example.dsl.code.function;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class FunctionUtils {

    public static <T> Optional<T> optional(T target) {
        return Optional.ofNullable(target);
    }

    public static <T> Stream<T> stream(Collection<T> targets) {
        return targets.stream();
    }

    public static <T, R> R of(T resource , Function<T, R> function) {
        return function.apply(resource);
    }

    public static <R> R of(Supplier<R> supplier) {
        return supplier.get();
    }

    public static <T> Boolean check(T value, Predicate<T> predicate) {
        return predicate.test(value);
    }

    public static <T> void consume(T resource, Consumer<T> consumer) {
        consumer.accept(resource);
    }
}
