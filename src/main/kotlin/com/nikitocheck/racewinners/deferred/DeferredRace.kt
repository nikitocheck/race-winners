package com.nikitocheck.racewinners.deferred

import kotlinx.coroutines.Deferred

interface DeferredRace {

    /**
     *
     * @param deferredList tasks that take part in race
     * @param winnersCount count of winners
     * @return results of first winnersCount completed futures
     * @param <T> return type of result
     */
    suspend fun <T> getWinners(deferredList: List<Deferred<T>>, winnersCount: Int): List<T>
}