package edu.quinnipiac.ser210.githubchat.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    public static String tryGetString(JSONObject object, String key) {
        try {
            return object.has(key) ? checkStringNull(object.getString(key)) : null;
        } catch (JSONException e) {
            return null;
        }
    }

    public static String checkStringNull(String string) {
        if (string == null || string.equals("null")) {
            return null;
        } else {
            return string;
        }
    }

    public static <T> T tryOrDefault(JsonTask<T> method, T defaultValue) {
        try {
            return method.run();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public interface JsonTask<T> {

        T run() throws Exception;
    }
}
