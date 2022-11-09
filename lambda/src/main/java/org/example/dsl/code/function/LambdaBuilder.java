package org.example.dsl.code.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaBuilder<T> {

    private T value;
    private Boolean stop;

    private LambdaBuilder(T value) {
        this.value = value;
        this.stop = false;
    }

    public static <T> LambdaBuilder<T> start(T value) {
        return new LambdaBuilder<>(value);
    }

    public LambdaBuilder<?> of(Function<T, ?> function) {
        if (this.stop) return this;
        return new LambdaBuilder<>(function.apply(this.value));
    }

    public LambdaBuilder<?> of(Supplier<?> supplier) {
        if (this.stop) return this;
        return new LambdaBuilder<>(supplier.get());
    }

    public LambdaBuilder<T> consume(Consumer<T> consumer) {
        if (this.stop) return this;
        consumer.accept(this.value);
        return this;
    }

    public LambdaBuilder<T> check(Predicate<T> predicate) {
        boolean nonStop = predicate.test(this.value);
        if (nonStop) {
            this.stop = false;
        } else {
            this.stop = true;
        }
        return this;
    }

    public T end() {
        return this.value;
    }

}
