package com.example.whanime.api;

import com.google.gson.*;
import java.lang.reflect.Type;

// Implementación de un deserializador personalizado para la clase Episode
public class EpisodeDeserializer implements JsonDeserializer<String> {

    // Método que deserializa un elemento JSON en un String
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // Si el elemento JSON es un array, se concatenan los elementos en un solo String
        if (json.isJsonArray()) {
            StringBuilder episodes = new StringBuilder();
            for (JsonElement element : json.getAsJsonArray()) {
                if (episodes.length() > 0) {
                    episodes.append(", ");
                }
                episodes.append(element.getAsString());
            }
            return episodes.toString();

            // Si el elemento JSON es un primitivo, se retorna su valor como String
        } else if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return primitive.getAsString();
            } else if (primitive.isString()) {
                return primitive.getAsString();
            }
        }

        // En cualquier otro caso, se retorna null
        return null;
    }
}