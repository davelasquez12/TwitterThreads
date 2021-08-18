package com.company;

import com.company.model.TweetSearch;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Process {
    protected Map<String, Integer> tweetCountMap;
    protected TwitterAPI recentTweetsAPI;
    protected String[] phrases;

    public Process(String...phrases) {
        this.phrases = phrases;
        initTwitterAPI();
    }

    private void initTwitterAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recentTweetsAPI = retrofit.create(TwitterAPI.class);
    }

    public List<String> buildQueries(String... phrasesToSearch) {
        List<String> queries = new ArrayList<>(phrasesToSearch.length);

        //only get original tweets in English (not part of a retweet, reply, and does not have any links)
        for(String phrase : phrasesToSearch) {
            queries.add("\"" + phrase + "\" lang:en -is:retweet -is:reply -has:links");
        }

        return queries;
    }

    protected Void runQuery(String query, String queryPhrase) throws IOException {
        String nextToken = null;
        System.out.printf("Calling API for tweets using the phrase: \"%s\" \n", queryPhrase);
        do {
            Call<TweetSearch> tweetSearchCall = recentTweetsAPI.getRecentTweetsByQuery(query, nextToken);
            Response<TweetSearch> tweetSearchResponse = tweetSearchCall.execute();
            TweetSearch tweetSearchObj = processResponse(tweetSearchResponse, queryPhrase);
            nextToken = tweetSearchObj.getMeta().getNextToken();

        } while(nextToken != null);

        return null;
    }

    protected TweetSearch processResponse(Response<TweetSearch> tweetSearchResponse, String queryPhrase) throws IOException {
        TweetSearch tweetSearch = null;

        if(tweetSearchResponse.isSuccessful()) {
            tweetSearch = tweetSearchResponse.body();
            if(tweetSearch != null && tweetSearch.getMeta() != null) {
                int totalCount = tweetCountMap.getOrDefault(queryPhrase, 0) + tweetSearch.getMeta().getResultCount();
                tweetCountMap.put(queryPhrase, totalCount);
            }else {
                System.out.println("tweetSearch obj is null or tweetSearch.getData() is null");
            }
        }else {
            System.out.println(tweetSearchResponse.errorBody().string());
        }
        return tweetSearch;
    }

    protected void outputResults() {
        System.out.println("TWEET COUNT TOTALS (past 7 days):");
        for(String key : tweetCountMap.keySet()) {
            System.out.printf("\"%s\" tweet count = %d%n", key, tweetCountMap.get(key));
        }
        System.out.println();
    }
}
