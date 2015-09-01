package net.jnmap;

import net.jnmap.data.dao.ScanJobDAO;
import net.jnmap.data.dao.ScanPortResultDAO;
import net.jnmap.parser.ScanPortResultParser;
import net.jnmap.scanner.Config;
import net.jnmap.scanner.Job;
import net.jnmap.scanner.Scanner;
import net.jnmap.scanner.ScannerFactory;
import net.jnmap.util.FutureUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scanner services container
 * <p/>
 * Created by lucas.
 */
public class ScannerService {
    private final Scanner scanner;

    private final ScanJobDAO scanJobDAO;
    private final ScanPortResultDAO scanResultDAO;
    private final ScanPortResultParser scanPortResultParser;

    public ScannerService(Scanner scanner,
                          ScanJobDAO scanJobDAO,
                          ScanPortResultDAO scanPortResultDAO,
                          ScanPortResultParser scanPortResultParser) {
        this.scanner = scanner;
        this.scanJobDAO = scanJobDAO;
        this.scanResultDAO = scanPortResultDAO;
        this.scanPortResultParser = scanPortResultParser;
    }

    /**
     * Returns historical scan results based given target and maximum report day count
     *
     * @param targets
     * @param maxReportDayCount
     * @return
     */
    public List<Job> getScanResults(String[] targets, int maxReportDayCount, int maxTargetCountPerReq) {
        return Stream.of(targets)
                .parallel()
                .distinct()
                .limit(maxTargetCountPerReq)
                .map(target -> scanJobDAO.get(target, maxReportDayCount))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Creates scan job entry in database, initiate scan and store result information,
     * concurrently when applicable
     *
     * @param targets
     * @param maxConcurrentScan
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Job> doScan(String[] targets, int maxConcurrentScan) throws ExecutionException, InterruptedException {
        // Parse target, create and run scan jobs, parse result before returning JSON
        List<CompletableFuture<Job>> scanPortResultFutureList = Stream.of(targets)
                .parallel()
                .distinct()
                .limit(maxConcurrentScan)
                .map(target -> CompletableFuture.supplyAsync(() -> scanJobDAO.create(target, scanner.getFullCommandLine(target))))
                .map(job -> job.thenApplyAsync(scanner::scan))
                .map(job -> job.thenApplyAsync(scanPortResultParser::parse))
                .collect(Collectors.<CompletableFuture<Job>>toList());

        // Wait till all job completes to obtain list of job results
        List<Job> completedJobList = FutureUtils.sequence(scanPortResultFutureList).get();

        // Persist the scan port results and update scan job asynchronously
        completedJobList.parallelStream()
                .forEach(completedJob -> CompletableFuture.runAsync(() -> {
                    if (null == completedJob.getResult() || CollectionUtils.isEmpty(completedJob.getResult().getPorts())) {
                        System.out.println("No port information for " + completedJob.getTarget());
                    }
                    else{
                        // Store port scan results
                        scanResultDAO.create(completedJob.getId(), completedJob.getResult());

                        // Update scan job table with host and completion information
                        scanJobDAO.update(completedJob);
                    }
                }));
        return completedJobList;
    }
}
