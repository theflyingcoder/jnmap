package net.jnmap.scanner;

/**
 * Scanner config interface
 *
 * Created by lucas.
 */
public interface Config {
    String getPath();

    String getOptions();

    String getCommandLinePrefix();
}
