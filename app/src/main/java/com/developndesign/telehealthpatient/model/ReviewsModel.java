package com.developndesign.telehealthpatient.model;

import com.google.gson.annotations.SerializedName;

public class ReviewsModel {
    @SerializedName("total")
    private long total;
    @SerializedName("rating")
    private long rating;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getRating() {
        return rating;
    }
}
