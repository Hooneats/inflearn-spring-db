package org.example.dsl.code.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 함수의 영속을 유지 할 것 이면 map 메서드를 사용해 새로운 객체를 항상 반환하도록 만들고,
 * 그게 아니라면 consume 메서드를 이용할 수 있다.
 * 이러한 속성은 stream 의 map 과 foreach 와 마찬가지 이다.
 */
@SuppressWarnings({"unchecked", "BooleanMethodIsAlwaysInverted"})
public final class Action<T> {

    private static final Action<?> EMPTY = new Action<>();

    private final T value;

    private Action() {
        this.value = null;
    }

    private Action(T value) {
        this.value = value;
    }

    public static <T> Action<T> start(T value) {
        return new Action<>(value);
    }

    public <U> Action<U> map(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        if (!isPresent()) {
            return empty();
        } else {
            return Action.ofNullable(function.apply(this.value));
        }
    }

    public Action<T> consume(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        if (!isPresent()) {
            return empty();
        } else {
            consumer.accept(this.value);
            return this;
        }
    }

    public Action<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(this.value) ? this : empty();
        }
    }

    public T end() {
        return this.value;
    }

    public Stream<T> stream() {
        if (!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(this.value);
        }
    }

    private static<T> Action<T> empty() {
        return (Action<T>) EMPTY;
    }

    private static <T> Action<T> of(T value) {
        return new Action<>(value);
    }

    private static <T> Action<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }


    private boolean isPresent() {
        return this.value != null;
    }

}
