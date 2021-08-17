package com.company;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class ProcessAsync extends Process{
    public ProcessAsync(String... phrases) {
        super(phrases);
        tweetCountMap = new ConcurrentHashMap<>(phrases.length * 2 + 1);
    }

    public void run() throws ExecutionException, InterruptedException {
        System.out.println("Running ASYNC process...");
        List<String> queries = buildQueries(phrases);
        runQueries(queries);
    }

    private void runQueries(List<String> queries) throws ExecutionException, InterruptedException {
        CompletableFuture<Void>[] completableFutures = new CompletableFuture[queries.size()];
        long start = System.currentTimeMillis();

        for(int i = 0; i < queries.size(); i++) {
            int finalI = i;

            CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return runQuery(queries.get(finalI), phrases[finalI]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            completableFutures[i] = completableFuture;
        }

        System.out.println("Executing futures...\n");
        CompletableFuture<Void> joined = CompletableFuture.allOf(completableFutures);
        joined.get();
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Futures have completed. Elapsed time = " + elapsedTime + "ms \n");

        outputResults();
    }
}
