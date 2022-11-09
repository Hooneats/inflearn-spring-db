package org.example.dsl.code.function;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class LambdaUtils<T> {

    public static <T> Optional<T> toOptional(T target) {
        return Optional.ofNullable(target);
    }

    public static <T> Stream<T> toStream(Collection<T> targets) {
        return targets.stream();
    }
}
