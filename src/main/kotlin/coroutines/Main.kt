import kotlinx.coroutines.*

fun main(args: Array<String>) {
    deferredExample()
}


fun firstCoroutine() = runBlocking {
    repeat(50_000) {
        doWorld()
        println("Done")
    }
}

suspend fun doWorld() = coroutineScope {
    val job2 = launch {
        //delay(2000L)
        println("World 2")
    }
    val job1 = launch {
        //delay(1000L)
        println("World 1")
    }
    println("Hello")
    job1.join()
    println("Done1")
    job2.join()
    println("Done2")

}

fun TimeoutExample() {
    var result = 0
    runBlocking {
        repeat(10_000) {
            launch {
                try {
                    withTimeout(50L) {
                        result++
                        delay(5L)
                    }
                } finally {
                    result--
                }

            }
        }

    }
    println(result)
}

fun cancelExample() {
    println("Start")
    runBlocking {
        val job = launch {
            try {
                repeat(100) {
                    println("Request $it")
                    delay(500L)
                }
            } finally {
                println("Requests cancelled")
            }

        }
        delay(2000L)
        job.cancelAndJoin()
        println("End")
    }
}

fun deferredExample() {
    runBlocking {
        println("Start")
        val deferredList: List<Deferred<Int>> = (1..3).map {
            async {
                delay(1000L * it)
                println("Loading $it")
                it
            }
        }
        println(deferredList.awaitAll().sum())
    }
}