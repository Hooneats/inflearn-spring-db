package org.example.dsl.code.function;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class LambdaUtils<T> {

    public static <V> Optional<V> toOptional(V target) {
        return Optional.ofNullable(target);
    }

    public static <V> Stream<V> toStream(Collection<V> targets) {
        return targets.stream();
    }
}
