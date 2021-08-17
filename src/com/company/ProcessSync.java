package com.company;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProcessSync extends Process {
    public ProcessSync(String... phrases) {
        super(phrases);
        tweetCountMap = new HashMap<>(phrases.length * 2 + 1);
    }

    public void run() throws IOException {
        System.out.println("Running SYNC process...");
        List<String> queries = buildQueries(phrases);
        runQueries(queries);
    }

    private void runQueries(List<String> queries) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("Executing queries in sync...\n");

        for(int i = 0; i < queries.size(); i++) {
            runQuery(queries.get(i), phrases[i]);
        }

        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Queries have completed. Elapsed time = " + elapsedTime + "ms \n");
        outputResults();
    }
}
