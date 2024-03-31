package coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


fun main() {
    flowsChangeContextConsumingExample()
}

fun flowsSimpleExample() {
    runBlocking {
        launch {
            for (k in 1..3) {
                println("Not blocked $k")
                delay(100)
            }
        }
        simple().collect() {
            println("Result $it")
        }
    }
}

fun flowsColdExample() {
    runBlocking {
        println("Calling simple")
        val flow = simple()
        println("Collecting")
        flow.collect { println(it) }
        println("Collecting again")
        flow.collect { println(it) }
    }
}

fun flowsCancelExample() {
    runBlocking {
        withTimeoutOrNull(250) {
            simple().collect { println(it) }
        }
        println("End")

        println("FlowBuilder")
        (1..3).asFlow().collect {
            println(it)
        }
    }

}

fun flowsTransformExample() {
    runBlocking {
        simple()
            .transform {
                emit("Type 1: $it")
                emit("Just int $it")
            }
            .collect {
                println(it)
            }
    }
}

fun flowsSizeLimitExample() {
    runBlocking {
        simple()
            .take(2)
            .collect() {
                println(it)
            }
        println("End")
    }
}

fun flowTerminalOperatorsExample() {
    runBlocking {
        val sum = (1..5).asFlow()
            .map { it * it }
            .reduce { a, b -> a + b } // reduce uses 1st element as accumulator, fold can use custom value
        println(sum)
    }
}

fun flowSequentialExample(){
    runBlocking {
        (1..5).asFlow()
            .filter {
                println("Filter $it")
                it % 2 == 0
            }
            .map {
                println("Map $it")
                "String $it"
            }
            .collect{
                println(it)
            }
    }
}

fun flowsChangeContextConsumingExample(){
    runBlocking {
        context().collect{
            log("Collected $it")
        }
    }
}

fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun context(): Flow<Int> = flow<Int> {
    for (i in 1..3){
        Thread.sleep(100)
        log("Emitting $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default)

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")