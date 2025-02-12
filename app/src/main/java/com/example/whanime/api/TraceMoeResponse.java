package com.example.whanime.api;

import java.util.List;

public class TraceMoeResponse {
    public List<Result> result;

    public static class Result {
        public String filename;
        public int episode;
        public String image;
    }
}
