package net.jnmap.scanner;

import net.jnmap.scanner.nmap.NMapConfig;
import net.jnmap.scanner.nmap.NMapScanner;

/**
 * Created by lucas on 8/29/15.
 */
public class ScannerFactory {
    public static Scanner createScanner(Config config) {
        Scanner scanner;
        if (config instanceof NMapConfig) {
            scanner = new NMapScanner((NMapConfig) config);
        }
        else {
            throw new UnsupportedOperationException("No scanner configured for config:" + config);
        }
        return scanner;
    }
}
