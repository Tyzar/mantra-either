package com.nokotogi.mantra.either.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nokotogi.mantra.either.Either
import com.nokotogi.mantra.either.app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.fetchbtn).setOnClickListener {
            simulateFetch()
        }
    }

    private fun simulateFetch() {
        lifecycleScope.launch {
            getEvenNumbers(20, 100)
                .pipeRight {
                    qualifyANumber(it)
                }.fold(
                    onLeft = {
                        Log.e("Either", it)
                    },
                    onRight = {
                        Log.i("Either", "Number $it is Qualified")
                    }
                )
        }
    }

    private suspend fun getEvenNumbers(min: Int, max: Int): Either<String, List<Int>> {
        delay(3000)
        if (min >= max) {
            return Either.Left("Failed to get even numbers")
        }

        val evens = mutableListOf<Int>()
        for (i in min..max) {
            if (i % 2 == 0) {
                evens.add(i)
            }
        }

        return Either.Right(evens)
    }

    private suspend fun qualifyANumber(evenNumbers: List<Int>): Either<String, Int> =
        withContext(Dispatchers.Default) {
            val randIdx = Random.nextInt(evenNumbers.indices)
            val selectedNum = evenNumbers[randIdx]
            return@withContext if (selectedNum % 2 == 0 && selectedNum % 3 == 0) {
                Either.Right(selectedNum)
            } else {
                Either.Left("This number $selectedNum is not qualified!")
            }
        }
}