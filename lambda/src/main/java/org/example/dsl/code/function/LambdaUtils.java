package org.example.dsl.code.function;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class LambdaUtils<T> {

    public static <V> Lambda<V> object(V target) {
        return new Lambda<>(Optional.ofNullable(target), Stream.empty());
    }

    public static <V> Lambda<V> collection(Collection<V> targets) {
        return new Lambda<>(Optional.empty(), targets.stream());
    }

    public static class Lambda<V> {

        private final Optional<V> optionalV;
        private final Stream<V> vStream;

        public Lambda(Optional<V> optionalV, Stream<V> vStream) {
            this.optionalV = optionalV;
            this.vStream = vStream;
        }

        public Optional<V> optional() {
            return optionalV;
        }

        public Stream<V> stream() {
            return vStream;
        }
    }
}
