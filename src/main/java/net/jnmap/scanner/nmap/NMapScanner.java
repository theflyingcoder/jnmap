package net.jnmap.scanner.nmap;


import net.jnmap.scanner.Job;
import net.jnmap.scanner.Result;
import net.jnmap.scanner.Scanner;
import net.jnmap.util.ConverterUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Wrapper for nmap process execution, upon completion of execution,
 * <code>ExecutionResults</code> containing the outputs and/or errors
 * will be returned to caller.
 * <p/>
 * Created by lucas on 8/28/15.
 */
public class NMapScanner implements Scanner {

    private final NMapConfig config;

    /**
     * Constructor for nmap execution instance with the given config.
     *
     * @param config
     * @throws NMapExecutionException
     */
    public NMapScanner(NMapConfig config) {
        if (null == config) {
            throw new NMapExecutionException("Properties to NMap execution is null");
        }
        this.config = config;

        if (StringUtils.isEmpty(config.getPath())) {
            throw new NMapExecutionException("Missing full path to nmap executable (NMAP_PATH)", config);
        }
    }

    /**
     * Executes nmap command line call based on the config.
     *
     * @return
     * @throws NMapExecutionException
     */
    public Job scan(Job job) {
        if (job  == null) {
            return null;
        }
        long startScanTime = System.currentTimeMillis();
        System.out.println("Scanning " + job.getTarget() + "...");
        String fullCommandLine = config.getFullCommandLine(job.getTarget());
        try {
            Process nmapProcess = Runtime.getRuntime().exec(fullCommandLine);
            job.setOutputs(ConverterUtils.inputStreamToString(nmapProcess.getInputStream()));
            job.setErrors(ConverterUtils.inputStreamToString(nmapProcess.getErrorStream()));
        } catch (IOException e) {
            throw new NMapExecutionException(e.getMessage(), e, fullCommandLine);
        }
        System.out.println("Scan Time [" + job.getTarget() + "]: " + (System.currentTimeMillis() - startScanTime) + "ms");
        return job;
    }

}
