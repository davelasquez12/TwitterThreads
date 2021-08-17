package com.company.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("newest_id")
    @Expose
    private String newestId;

    @SerializedName("oldest_id")
    @Expose
    private String oldestId;

    @SerializedName("next_token")
    @Expose
    private String nextToken;

    @SerializedName("result_count")
    @Expose
    private Integer resultCount;

    public String getNewestId() {
        return newestId;
    }

    public String getOldestId() {
        return oldestId;
    }

    public String getNextToken() {
        return nextToken;
    }

    public Integer getResultCount() {
        return resultCount;
    }
}
