package com.example.whanime.api;

import java.util.List;

public class TraceMoeResponse {
    public List<Result> result;

    public static class Result {
        public String filename;
        public String episode; // Change to String to handle various formats
        public String image;
    }
}