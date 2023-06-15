package com.nikitocheck.racewinners.futures;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

class CompletableFutureWinnersTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Random random = new Random();

    @Test
    public void testCompletableFuturesRaceLockImpl() {
        testWinners(new CompletableFuturesRaceLockImpl());
    }

    @Test
    public void testCompletableFutureRaceSyncQueueImpl() {
        testWinners(new CompletableFutureRaceSyncQueueImpl());
    }

    @Test
    public void testCompletableFuturesRaceLockImplKt() {
        testWinners(new CompletableFuturesRaceLockImplKt());
    }


    private void testWinners(CompletableFuturesRace completableFuturesRace) {

        var tasks = new ArrayList<CompletableFuture<Integer>>();

        for (int i = 0; i < 10; i++) {
            tasks.add(CompletableFuture.supplyAsync(task(i), executor));
        }

        var winners = completableFuturesRace.getWinners(tasks, 5);

        System.out.println("======== RESULT IS :" + winners);
    }

    private Supplier<Integer> task(int number) {
        return () -> {
            var delay = random.nextInt(5000, 10000);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("number %s. delay: %s. time: %s%n", number, delay, Instant.now());

            return number;
        };
    }

}