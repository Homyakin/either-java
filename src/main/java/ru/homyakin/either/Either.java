package ru.homyakin.either;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Either<L, R> {

    static <L, R> Either<L, R> left(L l) {
        return new Left<>(l);
    }

    static <L, R> Either<L, R> right(R r) {
        return new Right<>(r);
    }

    boolean isLeft();

    boolean isRight();

    default L left() {
        throw new IllegalStateException("Not a left");
    }

    default R right() {
        throw new IllegalStateException("Not a right");
    }

    <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper);

    <U> Either<L,? extends U> flatMap(Function<? super R, Either<L, ? extends U>> mapper);

    <U> Either<? extends U, R> flatMapLeft(Function<? super L, Either<? extends U, R>> mapper);

    <U> Either<L, U> map(Function<? super R, ? extends U> mapper);

    <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper);

    Either<L, R> peek(Consumer<? super R> action);

    Either<L, R> peekLeft(Consumer<? super L> action);

    record Left<L, R>(
        L l
    ) implements Either<L, R> {
        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L left() {
            return l;
        }

        @Override
        public <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
            Objects.requireNonNull(leftMapper, "leftMapper is null");
            Objects.requireNonNull(rightMapper, "rightMapper is null");
            return leftMapper.apply(l);
        }

        @Override
        public <U> Either<L,? extends U> flatMap(Function<? super R, Either<L, ? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return new Left<>(l);
        }

        @Override
        public <U> Either<? extends U, R> flatMapLeft(Function<? super L, Either<? extends U, R>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return mapper.apply(l);
        }

        @Override
        public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return new Left<>(l);
        }

        @Override
        public <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "leftMapper is null");
            return new Left<>(mapper.apply(l));
        }

        @Override
        public Either<L, R> peek(Consumer<? super R> action) {
            Objects.requireNonNull(action, "action is null");
            return this;
        }

        @Override
        public Either<L, R> peekLeft(Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            action.accept(l);
            return this;
        }
    }

    record Right<L, R>(
        R r
    ) implements Either<L, R> {
        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public R right() {
            return r;
        }

        @Override
        public <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
            Objects.requireNonNull(leftMapper, "leftMapper is null");
            Objects.requireNonNull(rightMapper, "rightMapper is null");
            return rightMapper.apply(r);
        }


        @Override
        public <U> Either<L,? extends U> flatMap(Function<? super R, Either<L, ? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return mapper.apply(r);
        }

        @Override
        public <U> Either<U, R> flatMapLeft(Function<? super L, Either<? extends U, R>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return new Right<>(r);
        }

        @Override
        public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return Either.right(mapper.apply(r));
        }

        @Override
        public <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "leftMapper is null");
            return new Right<>(r);
        }

        @Override
        public Either<L, R> peek(Consumer<? super R> action) {
            Objects.requireNonNull(action, "action is null");
            action.accept(r);
            return this;
        }

        @Override
        public Either<L, R> peekLeft(Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            return this;
        }
    }
}
