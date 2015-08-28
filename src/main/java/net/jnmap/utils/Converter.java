package net.jnmap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xephyr on 8/27/15.
 */
public class Converter {
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while (null != (line = bufferedReader.readLine())) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }
}
