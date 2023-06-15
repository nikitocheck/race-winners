package com.nikitocheck.racewinners.deferred

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeferredRaceChannelImpl: DeferredRace {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun <T> getWinners(deferredList: List<Deferred<T>>, winnersCount: Int): List<T> = coroutineScope {

        val channel = Channel<T>()

        deferredList.map { item ->
            launch {
                val value = item.await()
                if (!channel.isClosedForSend) {
                    channel.send(value)
                }
            }
        }

        val results = (1..winnersCount).map { channel.receive() }

        channel.close()

        deferredList.forEach { it.cancel("don't need anymore") }

        return@coroutineScope results
    }
}