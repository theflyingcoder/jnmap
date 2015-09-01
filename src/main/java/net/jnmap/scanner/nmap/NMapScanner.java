package net.jnmap.scanner.nmap;


import net.jnmap.scanner.Job;
import net.jnmap.scanner.Scanner;
import net.jnmap.util.ConverterUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Wrapper for nmap process execution, upon completion of execution,
 * <code>ExecutionResults</code> containing the outputs and/or errors
 * will be returned to caller.
 * <p/>
 * Created by lucas.
 */
public class NMapScanner implements Scanner {

    private final String fullCommandLine;

    /**
     * Constructor for nmap execution instance with the given config.
     *
     * @param fullCommandLine
     * @throws NMapExecutionException
     */
    public NMapScanner(String fullCommandLine) {
        if (StringUtils.isEmpty(fullCommandLine)) {
            throw new NMapExecutionException("Properties to NMap execution is null");
        }
        this.fullCommandLine = fullCommandLine;
    }

    public String getFullCommandLine(String target) {
        return fullCommandLine + " " + target;
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
        try {
            Process nmapProcess = Runtime.getRuntime().exec(getFullCommandLine(job.getTarget()));
            job.setOutputs(ConverterUtils.inputStreamToString(nmapProcess.getInputStream()));
            job.setErrors(ConverterUtils.inputStreamToString(nmapProcess.getErrorStream()));
        } catch (IOException e) {
            throw new NMapExecutionException(e.getMessage(), e, fullCommandLine);
        }
        System.out.println("Scan Time [" + job.getTarget() + "]: " + (System.currentTimeMillis() - startScanTime) + "ms");
        return job;
    }

}
