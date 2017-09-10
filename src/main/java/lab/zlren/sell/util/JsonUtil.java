package lab.zlren.sell.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by zlren on 17/9/10.
 */
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
