package com.gengzi.ui.save;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Optional;

public class JsonContentExtractor {
    public static String parse(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject choicesObject = jsonObject.getAsJsonArray("choices").get(0).getAsJsonObject();
        JsonObject messageObject = choicesObject.get("delta").getAsJsonObject();
        String content = messageObject.get("content").getAsString();
        System.out.println(content);
        return content;
    }
}