package com.example.karthikeyanp.popularmovies;

import java.io.Serializable;

/**
 * Created by karthikeyanp on 2/17/2017.
 */

public enum SortOrder implements Serializable {
    TOP_RATED("top_rated"),
    POPULAR("popular"),
    FAV("fav");

    public String sortOrder;

    private SortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getValue() {
        return sortOrder;
    }
}
