package org.example.dsl.code.function;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaBuilder {

    private Optional<?> value;

    private <T> LambdaBuilder(T value) {
        this.value = Optional.ofNullable(value);
    }

    public static <T> LambdaBuilder start(T value) {
        return new LambdaBuilder(value);
    }

    public <T> LambdaBuilder of(Function<Object, ?> function) {
        if (this.value.isEmpty()) return this;
        return new LambdaBuilder(function.apply(this.value.get()));
    }

    public LambdaBuilder of(Supplier<?> supplier) {
        if (this.value.isEmpty()) return this;
        return new LambdaBuilder(supplier.get());
    }

    public <T> LambdaBuilder consume(Consumer<Object> consumer) {
        if (this.value.isEmpty()) return this;
        consumer.accept(this.value.get());
        return this;
    }

    public <T> LambdaBuilder check(Predicate<Object> predicate) {
        if (this.value.isEmpty()) return this;
        boolean stop = !predicate.test(this.value.get());
        if (stop) {
            this.value = Optional.empty();
        }
        return this;
    }

    public Optional<?> end() {
        if (this.value.isEmpty()) return Optional.empty();
        return this.value;
    }

}
