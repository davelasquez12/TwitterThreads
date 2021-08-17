package com.company.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TweetSearch {
    @SerializedName("data")
    @Expose
    private List<TweetInfo> data;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<TweetInfo> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}
