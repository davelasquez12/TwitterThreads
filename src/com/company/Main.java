package com.company;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

	    // Build and format 3 twitter search queries

        // Loop through queries. For each query start a CompletableFuture chain. Each chain will page through the result set asynchronously.

        // Combine all Future objects into one using CompletableFuture.allOf(future1...futureN), then call combinedFutures.get()

        // Get the result counts from each future

        ProcessAsync processAsync = new ProcessAsync();
        processAsync.run();
    }
}
