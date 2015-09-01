package net.jnmap.scanner;

import net.jnmap.scanner.nmap.NMapConfig;
import net.jnmap.scanner.nmap.NMapScanner;

/**
 * Created by lucas.
 */
public class ScannerFactory {
    public static Scanner createScanner(Config config) {
        Scanner scanner;
        if (config instanceof NMapConfig) {
            scanner = new NMapScanner(config.getCommandLinePrefix());
        }
        else {
            throw new UnsupportedOperationException("No scanner configured for config:" + config);
        }
        return scanner;
    }
}
