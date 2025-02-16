package com.example.whanime.api;

import java.util.List;

// Clase que representa la respuesta de la API de TraceMoe
public class TraceMoeResponse {
    public List<Result> result; // Lista de resultados

    // Clase que representa un resultado individual
    public static class Result {
        public String filename; // Nombre del archivo
        public String episode; // Episodio
        public String image; // URL de la imagen
        public String video; // URL del video
        public Anilist anilist; // Información de AniList

        // Clase que representa la información de AniList
        public static class Anilist {
            public Title title; // Título

            // Clase que representa el título
            public static class Title {
                public String romaji; // Título en romaji, es decir, en japones pero letras latinas
            }
        }
    }
}