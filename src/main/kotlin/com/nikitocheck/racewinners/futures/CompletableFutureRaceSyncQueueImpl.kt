package com.nikitocheck.racewinners.futures

import java.util.concurrent.CompletableFuture
import java.util.concurrent.SynchronousQueue

class CompletableFutureRaceSyncQueueImpl : CompletableFuturesRace {

    override fun <T> getWinners(futures: List<CompletableFuture<T>>, winnersCount: Int): List<T> {
        val queue = SynchronousQueue<T>()
        val winners = ArrayList<T>()

        futures.forEach { future -> future.thenAccept { queue.put(it) } }

        repeat((1..winnersCount).count()) {
            winners.add(queue.take())
        }

        futures.forEach { it.cancel(true) }

        return winners
    }
}