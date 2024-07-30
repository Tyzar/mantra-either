package com.nokotogi.mantra.either

sealed class Either<L, R> {
    data class Left<L, R>(val leftValue: L) :
        Either<L, R>()

    data class Right<L, R>(val rightValue: R) :
        Either<L, R>()

    fun getLeft(): L? {
        return when (this) {
            is Left -> this.leftValue
            is Right -> null
        }
    }

    fun getRight(): R? {
        return when (this) {
            is Left -> null
            is Right -> this.rightValue
        }
    }

    fun isLeft(): Boolean {
        return when (this) {
            is Left -> true
            is Right -> false
        }
    }

    fun isRight(): Boolean {
        return when (this) {
            is Left -> false
            is Right -> true
        }
    }

    fun <T> fold(onRight: (rightValue: R) -> T, onLeft: (leftValue: L) -> T): T {
        return when (this) {
            is Left -> onLeft(this.leftValue)
            is Right -> onRight(this.rightValue)
        }
    }

    fun <M, N> flatMap(mapFunc: (value: Either<L, R>) -> Either<M, N>):
            Either<M, N> {
        return mapFunc(this)
    }

    fun <M, N> map(
        onRight: (rightValue: R) -> N,
        onLeft: (leftValue: L) -> M
    ): Either<M, N> {
        return when (this) {
            is Left -> Left(onLeft(this.leftValue))
            is Right -> Right(onRight(this.rightValue))
        }
    }

    fun <N> mapRight(onRight: (rightValue: R) -> N): Either<L, N> {
        return when (this) {
            is Left -> Left(this.leftValue)
            is Right -> Right(onRight(this.rightValue))
        }
    }

    fun <M> mapLeft(onLeft: (leftValue: L) -> M): Either<M, R> {
        return when (this) {
            is Left -> Left(onLeft(this.leftValue))
            is Right -> Right(this.rightValue)
        }
    }
}