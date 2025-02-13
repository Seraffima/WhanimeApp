package com.example.whanime.api;

import com.google.gson.*;
import java.lang.reflect.Type;

public class AnilistDeserializer implements JsonDeserializer<TraceMoeResponse.Result.Anilist> {
    @Override
    public TraceMoeResponse.Result.Anilist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            return new Gson().fromJson(json, TraceMoeResponse.Result.Anilist.class);
        } else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return null; // or handle the number case as needed
        }
        return null;
    }
}