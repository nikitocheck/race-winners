package com.nikitocheck.racewinners.futures;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CompletableFuturesRace {

    /**
     *
     * @param futures tasks that take part in race
     * @param winnersCount count of winners
     * @return results of first winnersCount completed futures
     * @param <T> return type of result
     */
     <T> List<T> getWinners(List<CompletableFuture<T>> futures, int winnersCount);
}
