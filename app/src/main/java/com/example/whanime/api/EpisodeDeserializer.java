package com.example.whanime.api;

import com.google.gson.*;
import java.lang.reflect.Type;

public class EpisodeDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            StringBuilder episodes = new StringBuilder();
            for (JsonElement element : json.getAsJsonArray()) {
                if (episodes.length() > 0) {
                    episodes.append(", ");
                }
                episodes.append(element.getAsString());
            }
            return episodes.toString();
        } else if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return primitive.getAsString();
            } else if (primitive.isString()) {
                return primitive.getAsString();
            }
        }
        return null;
    }
}