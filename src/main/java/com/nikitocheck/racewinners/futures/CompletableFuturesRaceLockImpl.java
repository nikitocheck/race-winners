package com.nikitocheck.racewinners.futures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class CompletableFuturesRaceLockImpl implements CompletableFuturesRace {

    public <T> List<T> getWinners(List<CompletableFuture<T>> futures, int winnersCount) {

        final var lock = new ReentrantLock();
        final var winners = new ArrayList<T>();
        var summary = CompletableFuture.runAsync(() -> {
            AtomicInteger completedCount = new AtomicInteger();

            for (var item : futures) {
                item.thenAccept((num) -> {
                    lock.lock();
                    if (completedCount.getAndIncrement() < winnersCount) {
                        winners.add(num);
                    }
                    lock.unlock();
                });
            }

            while (completedCount.get() < winnersCount) {
                //wait futures
            }
        });

        try {
            summary.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        futures.forEach((item) -> item.cancel(true));

        return winners;
    }
}
