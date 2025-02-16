package com.example.whanime.api;

import com.google.gson.*;
import java.lang.reflect.Type;

// Implementación de un deserializador personalizado para la clase Anilist dentro de TraceMoeResponse.Result
public class AnilistDeserializer implements JsonDeserializer<TraceMoeResponse.Result.Anilist> {

    // Método que deserializa un elemento JSON en un objeto Anilist
    @Override
    public TraceMoeResponse.Result.Anilist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // Si el elemento JSON es un objeto JSON, se deserializa utilizando Gson
        if (json.isJsonObject()) {
            return new Gson().fromJson(json, TraceMoeResponse.Result.Anilist.class);

            // Si el elemento JSON es un número primitivo, se retorna null
        } else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return null;
        }

        // En cualquier otro caso, se retorna null
        return null;
    }
}