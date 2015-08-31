package net.jnmap.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by lucas on 8/29/15.
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}