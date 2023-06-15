package com.nikitocheck.racewinners.deferred

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

class DeferredRaceFlowImpl : DeferredRace {

    override suspend fun <T> getWinners(deferredList: List<Deferred<T>>, winnersCount: Int): List<T> {
        val result = channelFlow {
            deferredList.map { launch { send(it.await()) } }
        }
            .take(winnersCount)
            .toCollection(mutableListOf())

        deferredList.forEach { it.cancel("don't need anymore") }

        return result
    }
}