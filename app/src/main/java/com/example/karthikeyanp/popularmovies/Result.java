package com.example.karthikeyanp.popularmovies;

import java.util.List;

public class Result {
    public List<Movie> mResultValue;
    public Exception mException;

    public Result(List<Movie> resultValue) {
        mResultValue = resultValue;
    }

    public Result(Exception exception) {
        mException = exception;
    }
}