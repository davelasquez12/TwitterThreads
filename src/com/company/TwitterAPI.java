package com.company;

import com.company.model.TweetSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TwitterAPI {
    @GET("2/tweets/search/recent?max_results=100")
    @Headers("Authorization: Bearer " + Keys.TWITTER_BEARER_TOKEN)
    Call<TweetSearch> getRecentTweetsByQuery(@Query(value = "query") String query, @Query("next_token") String nextToken);
}
