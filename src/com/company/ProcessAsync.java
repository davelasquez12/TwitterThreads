package com.company;

import com.company.model.TweetSearch;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class ProcessAsync extends Process{
    private ConcurrentHashMap<String, Integer> tweetCountMap;
    private String[] queryPhrases;

    public void run() throws ExecutionException, InterruptedException {
        initTwitterAPI();
        queryPhrases = new String[] {"pizza and wings", "batman and robin", "tom and jerry"};
        List<String> queries = buildQueries(queryPhrases);
        tweetCountMap = new ConcurrentHashMap<>(queries.size() * 2 + 1);
        runQueries(queries);
    }

    private void runQueries(List<String> queries) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<Void>[] completableFutures = new CompletableFuture[queries.size()];

        for(int i = 0; i < queries.size(); i++) {
            int finalI = i;

            CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return runQuery(queries.get(finalI), queryPhrases[finalI]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            completableFutures[i] = completableFuture;
        }

        CompletableFuture<Void> joined = CompletableFuture.allOf(completableFutures);
        System.out.println("Executing futures...\n");
        joined.get();
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Futures have completed. Elapsed time = " + elapsedTime + "ms \n");

        outputResults();
    }

    private Void runQuery(String query, String queryPhrase) throws IOException {
        String nextToken = null;
        do {
            System.out.printf("Calling API for tweets using the phrase: \"%s\"; nextToken = %s%n", queryPhrase, nextToken);
            Call<TweetSearch> tweetSearchCall = recentTweetsAPI.getRecentTweetsByQuery(query, nextToken);
            Response<TweetSearch> tweetSearchResponse = tweetSearchCall.execute();
            TweetSearch tweetSearchObj = processResponse(tweetSearchResponse, queryPhrase);
            nextToken = tweetSearchObj.getMeta().getNextToken();

        } while(nextToken != null);

        return null;
    }

    private TweetSearch processResponse(Response<TweetSearch> tweetSearchResponse, String queryPhrase) throws IOException {
        TweetSearch tweetSearch = null;

        if(tweetSearchResponse.isSuccessful()) {
            tweetSearch = tweetSearchResponse.body();
            if(tweetSearch != null && tweetSearch.getMeta() != null) {
                int totalCount = tweetCountMap.getOrDefault(queryPhrase, 0) + tweetSearch.getMeta().getResultCount();
                tweetCountMap.put(queryPhrase, totalCount);
                System.out.println("Total count for " + "\"" + queryPhrase + "\" is: " + totalCount);
            }else {
                System.out.println("tweetSearch obj is null or tweetSearch.getData() is null");
            }
        }else {
            System.out.println(tweetSearchResponse.errorBody().string());
        }
        return tweetSearch;
    }

    private void outputResults() {
        System.out.println("TWEET COUNT TOTALS (past 7 days):");
        for(String key : tweetCountMap.keySet()) {
            System.out.printf("\"%s\" tweet count = %d%n", key, tweetCountMap.get(key));
        }
    }
}
