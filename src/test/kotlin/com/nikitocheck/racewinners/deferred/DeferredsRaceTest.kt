package com.nikitocheck.racewinners.deferred

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.random.Random

internal class SuspendSumFirstNumbersServiceTest {


    @Test
    fun `test DeferredRaceChannelImpl`() = runBlocking {
        testDeferredRace(DeferredRaceChannelImpl())
    }

    @Test
    fun `test DeferredRaceFlowImpl`() = runBlocking {
        testDeferredRace(DeferredRaceFlowImpl())
    }

    private suspend fun CoroutineScope.testDeferredRace(race: DeferredRace) {
        val tasks = (1..10).map { busyTaskAsync(it).invoke(this) }
        val result = race.getWinners(tasks, 5)
        println("======== RESULT IS : $result")
    }

    private suspend fun busyTaskAsync(number: Int): suspend CoroutineScope.() -> Deferred<Int> {
        return {
            async(Dispatchers.IO) {
                val delay = Random.nextLong(from = 5000, until = 10000)
                //pretend some io
                delay(delay)
                println("number $number. delay: $delay. time: ${Instant.now()}")
                number
            }
        }
    }
}
