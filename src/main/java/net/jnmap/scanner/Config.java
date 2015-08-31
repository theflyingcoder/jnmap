package net.jnmap.scanner;

/**
 * Created by lucas on 8/29/15.
 */
public interface Config {
    String getPath();
    String getOptions();

    String getFullCommandLine(String target);
}
