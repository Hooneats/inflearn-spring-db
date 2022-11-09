package org.example.dsl.code.query;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.TemporalExpression;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class QueryDslHelper {

    /**
     * @return (QEntity1.date) eq (QEntity2.date)
     */
    public static <T extends Comparable,E extends Comparable> BooleanExpression eqDate(
        TemporalExpression<T> date1, TemporalExpression<E> date2
    ) {
        DateTemplate<String> dateTemplate1 = Expressions.dateTemplate(
            String.class,
            "DATE_FORMAT({0}, {1})",
            date1,
            ConstantImpl.create("%Y-%m-%d")
        );

        DateTemplate<String> dateTemplate2 = Expressions.dateTemplate(
            String.class,
            "DATE_FORMAT({0}, {1})",
            date1,
            ConstantImpl.create("%Y-%m-%d")
        );
        return dateTemplate1.eq(dateTemplate2);
    }

    public static <V> WhereCondition<V> optionalWhen(V value) {
        return new WhereCondition<>(value);
    }

    private static class WhereCondition<V> {

        private final V value;

        public WhereCondition(V value) {
            this.value = value;
        }

        public void then(Consumer<V> consumer) {
            Optional.ofNullable(value).ifPresent(consumer);
        }
    }
}
