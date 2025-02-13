package com.example.whanime.api;

import java.util.List;

public class TraceMoeResponse {
    public List<Result> result;

    public static class Result {
        public String filename;
        public String episode;
        public String image;
        public String video;
        public Anilist anilist;

        public static class Anilist {
            public Title title;

            public static class Title {
                public String romaji;
            }
        }
    }
}