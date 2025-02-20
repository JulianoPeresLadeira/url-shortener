package com.jules.urlshortener.util;

import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T, E extends Throwable> permits Result.Ok, Result.Err {
    record Ok<T, E extends Throwable>(T value) implements Result<T, E> {}
    record Err<T, E extends Throwable>(E error) implements Result<T, E> {}

    static <T, E extends Throwable> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E extends Throwable> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    default boolean isOk() {
        return this instanceof Ok<T, E>;
    }

    default boolean isErr() {
        return this instanceof Err<T, E>;
    }

    default T unwrap() {
        if (this instanceof Ok<T, E>(T value)) {
            return value;
        }
        throw new IllegalStateException("Called unwrap on an Err value");
    }

    default T getOrThrow() throws E {
        return switch (this) {
            case Err<T, E >(E error): {
                throw error;
            }
            case Ok<T, E>(T value): yield value;
        };
    }

    default E unwrapErr() {
        if (this instanceof Err<T, E>(E error)) {
            return error;
        }
        throw new IllegalStateException("Called unwrapErr on an Ok value");
    }

    default T unwrapOr(T defaultValue) {
        return this instanceof Ok<T, E>(T value) ? value : defaultValue;
    }

    default T unwrapOrElse(Function<E, T> fallback) {
        return this instanceof Ok<T, E>(T value) ? value : fallback.apply(((Err<T, E>) this).error);
    }

    default void ifOk(Consumer<T> consumer) {
        if (this instanceof Ok<T, E>(T value)) {
            consumer.accept(value);
        }
    }

    default void ifErr(Consumer<E> consumer) {
        if (this instanceof Err<T, E>(E error)) {
            consumer.accept(error);
        }
    }

    default <U> Result<U, E> map(Function<T, U> mapper) {
        return this instanceof Ok<T, E>(T value) ? Result.ok(mapper.apply(value)) : Result.err(((Err<T, E>) this).error);
    }

    default <F extends Throwable> Result<T, F> mapErr(Function<E, F> mapper) {
        return this instanceof Err<T, E>(E error) ? Result.err(mapper.apply(error)) : Result.ok(((Ok<T, E>) this).value);
    }
}
