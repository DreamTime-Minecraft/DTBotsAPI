package su.dreamtime.dtbotsapi.util;

import com.google.gson.Gson;

import java.util.HashMap;

public class JsonParser {
    private static Gson gson = new Gson();

    public static <T> T  parseJson(String json, Class<T> classType) {
        return gson.fromJson(json, classType);
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }
}
