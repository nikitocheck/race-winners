package com.nikitocheck.racewinners.futures

import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class CompletableFuturesRaceLockImplKt : CompletableFuturesRace {

    override fun <T> getWinners(items: List<CompletableFuture<T>>, winnersCount: Int): List<T> {

        val lock = ReentrantLock()
        val winners = ArrayList<T>()

        val summary = CompletableFuture.runAsync {

            val completedCount = AtomicInteger()

            items.forEach { future ->
                future.thenAccept {
                    lock.withLock {
                        if (completedCount.getAndIncrement() < winnersCount) {
                            winners.add(it)
                        }
                    }
                }
            }


            while (completedCount.get() < winnersCount) {
                //wait futures
            }
        }

        summary.get()

        items.forEach { it.cancel(true) }

        return winners
    }

}