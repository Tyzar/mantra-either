package com.nokotogi.mantra.either

import org.junit.Test
import org.junit.Assert

class EitherTest {

    @Test
    fun testCreateLeft() {
        val either: Either<String, Int> =
            Either.Left("Left")
        Assert.assertEquals(
            "Left",
            (either as Either.Left).leftValue
        )
    }

    @Test
    fun testCreateRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        Assert.assertEquals(
            10,
            (either as Either.Right).rightValue
        )
    }

    @Test
    fun testIsLeftWithRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        Assert.assertFalse(either.isLeft())
    }

    @Test
    fun testIsLeftWithLeft() {
        val either: Either<String, Int> =
            Either.Left("10")
        Assert.assertTrue(either.isLeft())
    }

    @Test
    fun testIsRightWithLeft() {
        val either: Either<String, Int> =
            Either.Left("21")
        Assert.assertFalse(either.isRight())
    }

    @Test
    fun testIsRightWithRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        Assert.assertTrue(either.isRight())
    }

    @Test
    fun testGetLeftWithRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        Assert.assertNull(either.getLeft())
    }

    @Test
    fun testGetLeftWithLeft() {
        val either: Either<String, Int> =
            Either.Left("10")
        Assert.assertEquals(
            "10",
            either.getLeft()
        )
    }

    @Test
    fun testGetRightWithLeft() {
        val either: Either<String, Int> =
            Either.Left("10")
        Assert.assertNull(either.getRight())
    }

    @Test
    fun testGetRightWithRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        Assert.assertEquals(
            10,
            either.getRight()
        )
    }

    @Test
    fun testFlatMapWithRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        val newEither =
            either.flatMap { value ->
                return@flatMap when (value) {
                    is Either.Left -> Either.Left(
                        "New Left"
                    )

                    is Either.Right -> Either.Right(
                        "Value ${value.rightValue}"
                    )
                }
            }

        Assert.assertEquals(
            "New right value assertion",
            "Value 10",
            newEither.getRight()
        )
    }

    @Test
    fun testFlatMapWithLeft() {
        val either: Either<String, Int> =
            Either.Left("10")
        val newEither =
            either.flatMap { value ->
                return@flatMap when (value) {
                    is Either.Left -> Either.Left(
                        "New Left ${value.leftValue}"
                    )

                    is Either.Right -> Either.Right(
                        "Value ${value.rightValue}"
                    )
                }
            }

        Assert.assertEquals(
            "New left value assertion",
            "New Left 10",
            newEither.getLeft()
        )
    }

    @Test
    fun testMapWithRight() {
        val either: Either<String, Int> =
            Either.Right(10)
        val newEither =
            either.map(onRight = {
                it * 2
            }, onLeft = {
                it
            })
        Assert.assertEquals(
            20,
            newEither.getRight()
        )
    }

    @Test
    fun testMapWithLeft() {
        val either: Either<String, Int> =
            Either.Left("30")
        val newEither =
            either.map(onRight = {
                it * 2
            }, onLeft = {
                it.toInt() * 2
            })
        Assert.assertEquals(
            60,
            newEither.getLeft()
        )
    }

    @Test
    fun testMapRightWithLeft() {
        val either: Either<String, Int> =
            Either.Left("30")
        val newEither =
            either.mapRight {
                it * 2
            }
        Assert.assertEquals(
            "30",
            newEither.getLeft()
        )
    }

    @Test
    fun testMapRightWithRight() {
        val either: Either<String, Int> =
            Either.Right(30)
        val newEither =
            either.mapRight {
                it * 2
            }
        Assert.assertEquals(
            60,
            newEither.getRight()
        )
    }

    @Test
    fun testMapLeftWithRight() {
        val either: Either<String, Int> =
            Either.Right(30)
        val newEither =
            either.mapLeft {
                "new left $it"
            }
        Assert.assertEquals(
            30,
            newEither.getRight()
        )
    }

    @Test
    fun testMapLeftWithLeft() {
        val either: Either<String, Int> =
            Either.Left("30")
        val newEither =
            either.mapLeft {
                it.toInt() * 2
            }
        Assert.assertEquals(
            60,
            newEither.getLeft()
        )
    }

    @Test
    fun testFoldWithRightReturnUnit() {
        val either: Either<String, Int> = Either.Right(30)
        either.fold(onLeft = { Assert.assertFalse(true) }, onRight = {
            Assert.assertTrue(true)
        })
    }

    @Test
    fun testFoldWithLeftReturnUnit() {
        val either: Either<String, Int> = Either.Left("30")
        either.fold(onLeft = { Assert.assertFalse(false) }, onRight = {
            Assert.assertFalse(true)
        })
    }

    @Test
    fun testFoldWithRightReturnValue() {
        val either: Either<String, Int> = Either.Right(30)
        val result = either.fold(onLeft = { "On 30" }, onRight = {
            it.toString()
        })
        Assert.assertEquals("30", result)
    }

    @Test
    fun testFoldWithLeftReturnValue() {
        val either: Either<String, Int> = Either.Left("30")
        val result = either.fold(onLeft = { "On 30" }, onRight = {
            it.toString()
        })
        Assert.assertEquals("On 30", result)
    }
}