[![](https://jitpack.io/v/Tyzar/mantra-either.svg)](https://jitpack.io/#Tyzar/mantra-either)



# Mantra Either

A simple Android kotlin library to wrap two different possible results as one value. Value wrapped as either Left or Right depend on how you assign the value to wrapper.

# Installation

## Gradle Maven

On `settings.gradle.kts`, add custom maven url to `dependencyResolutionManagement`
```
maven {
            url = uri("https://jitpack.io")
        }

```
or
```
maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.Tyzar")
            }
        }

```

On app module `build.gradle.kts` add dependency
```
implementation 'com.github.Tyzar:mantra-either:1.0.4'

```
# Usage

## Create `Either`
You can use `Either` as return value from function. Put each possible return value either at `Left` or `Right`.
```
fun generateImageData(): Either<Exception,ByteArray>{
        try {
            val data: ByteArray = imgGenerator.generate()
            return Either.Right(data)
        }catch (e:Exception){
            return Either.Left(e)
        }
    }

```
## Get `Either` Values

### Get `Left` Value
Get left value from `Either`. If `Either` is not `Left`, return `null` value.
```
fun handleDataError(result:Either<Exception,ByteArray>){
        val error = result.getLeft()
        if(error != null){
            showAlert(error)
        }
    }

```

### Get `Right` Value
Get right value from `Either`. If `Either` is not `Right`, return `null` value.
```
fun handleBytes(result:Either<Exception,ByteArray>){
        val bytes = result.getRight()
        if(bytes != null){
            //do data operation here...
        }
    }

```

### Using `Fold` Function
Another way to get and handle `Either` data is using `fold` function. `fold` function expects `onLeft` and `onRight` functions to be provided. 
`onLeft` provides left value and `onRight` provides right value of `Either`. These functions will be called according to `Either` state wheter `Left` or `Right`. 
One important thing that must be noted is `onLeft` and `onRight` functions must return same type value.

Below is example of using `fold` function with return value `Unit`.
```
fun handleBytes(result:Either<Exception,ByteArray>){
        result.fold(
            onLeft = {showAlert(it)},
            onRight = {bytes -> processData(bytes)}
        )
    }

```
This one uses return value other than `Unit`.
```
fun getMainSectionPage(result:Either<Exception,List<Product>>){
        val mainContent = result.fold(
            onLeft = {ErrorInfo(it)},
            onRight = {bytes -> ListProductContent(bytes)}
        )

        return SectionPage(mainContent)
    }

```

### Using Kotlin `When`
Another way is by using kotlin `when` to get and handle all either state.
```
fun displayMainSectionPage(result:Either<Exception,List<Product>>){
        val mainContent = when(result){
            is Either.Left -> ErrorInfo(result.leftValue)
            is Either.Right -> ListProductContent(result.rightValue) 
        }

        return SectionPage(mainContent)
    }

```

## Transform `Either` to another `Either`
`Either` can be transformed into another `Either` with different `Left` and `Right` value type.
### Transform using `Map`
```
 fun saveToCache(fetchResult: Either<Exception, ByteArray>): Either<String, List<Data>> {
        return fetchResult.map(
            onLeft = { it.message ?: "An error occurred" },
            onRight = { bytes ->
                decodeBytes(bytes)
            }
        )
    }

```

## Transform using `Pipe`
`Either` also can be transformed to another `Either` using `Pipe`. 
The main difference between `Map` and `Pipe` is the type of value that returned from function `onLeft` and `onRight`.
In `Pipe`, function `onLeft` and `onRight` expect return value type of `Either` instead. 
There are three variants of `Pipe` function, namely `pipe`, `pipeRight`, and `pipeLeft`.
Function `pipe` expects `onLeft` and `onRight` function to handle input value.
Function `pipeRight` only expects `onRight` function. Therefore the `Left` value will remains the same as input in pipe output result.
Function `pipeLeft` only expects `onLeft` function. Therefore the `Right` value will remains the same as input in pipe output result.
```
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

```
