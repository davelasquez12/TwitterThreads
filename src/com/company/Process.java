package com.company;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Process {
    protected TwitterAPI recentTweetsAPI;

    public void initTwitterAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recentTweetsAPI = retrofit.create(TwitterAPI.class);
    }

    public List<String> buildQueries(String... phrasesToSearch) {
        List<String> queries = new ArrayList<>(phrasesToSearch.length);

        for(String phrase : phrasesToSearch) {
            queries.add("\"" + phrase + "\" lang:en -is:retweet -is:reply -has:links");
        }

        return queries;
    }
}
