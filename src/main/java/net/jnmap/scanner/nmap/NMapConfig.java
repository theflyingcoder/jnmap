package net.jnmap.scanner.nmap;

import net.jnmap.scanner.Config;
import org.apache.commons.lang3.StringUtils;

/**
 * Simple representation of nmap command line.
 * <p>
 *     <code>pathToNMap</code> is path to point to the pathToNMap example: /usr/bin/nmap or C:/Program Files/nmap.exe
 *     <code>options</code> is scan type and options to be used to run the nmap scan
 *     <code>target</code> is target specification for the scan
 * </p>
 *
 * Created by lucas.
 */
public class NMapConfig implements Config {

    private final String path;
    private final String options;


    public NMapConfig(String nMapPath, String nMapOptions) {
        this.path = nMapPath;
        this.options = nMapOptions;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getOptions() {
        return options;
    }

    /**
     * Constructs the full executable command for nmap.
     *
     * @return
     */
    public String getCommandLinePrefix() {
        return getPath() + " " + addXmlOutputFlag();
    }

    /**
     * Adds xml output flag when necessary.
     *
     * @return
     */
    private StringBuilder addXmlOutputFlag() {
        StringBuilder optWithXmlOutputFlag = new StringBuilder(options);
        if (!StringUtils.contains(options, "-oX")) {
            if (!StringUtils.isEmpty(options)) {
                optWithXmlOutputFlag.append(" -oX -");
            }
        }
        return optWithXmlOutputFlag;
    }

    @Override
    public String toString() {
        return "NMapProperties{" +
                "pathToNMap='" +  + '\'' +
                ", options='" + options + '\'' +
                '}';
    }
}
