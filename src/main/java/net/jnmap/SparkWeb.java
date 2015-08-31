package net.jnmap;

import net.jnmap.data.dao.ScanJobDAO;
import net.jnmap.data.dao.ScanPortResultDAO;
import net.jnmap.data.dao.mysql.MySqlScanJobDAO;
import net.jnmap.data.dao.mysql.MySqlScanPortResultDAO;
import net.jnmap.parser.NMapXmlScanPortResultParser;
import net.jnmap.scanner.Config;
import net.jnmap.scanner.Job;
import net.jnmap.scanner.Scanner;
import net.jnmap.scanner.ScannerFactory;
import net.jnmap.util.FutureUtils;
import net.jnmap.util.JsonTransformer;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.*;

/**
 * Main class to instantiate spark web framework
 * <p/>
 * Created by lhalim on 8/27/15.
 */
public class SparkWeb {

    /**
     * Controller method where we do all the spark setup the routes and necessary resources.
     *
     * @param args
     */
    public static void main(String[] args) {
        port(Env.getAssignedPort());

        // Initialize data source
        final DataSource dataSource = Env.getDataSource();


        // GET Handler
        // Retrieves the scan results and history of the given targets
        get("/scan/:target/days/:days", (request, response) -> {
            final String target = request.params(":target");
            final int days = Integer.parseInt(request.params(":days"));
            int reportDayLimit = Env.getScanReportDaysLimit();
            int reportDayCount = Math.min(reportDayLimit, days);

            return new MySqlScanJobDAO(dataSource).get(target, reportDayCount);
        }, new JsonTransformer());


        // POST Handler
        // Scans the given delimited targets
        post("/scan/:targets", (request, response) -> {
            long startTime = System.currentTimeMillis();
            final String targets = request.params(":targets");

            // Setup for NMAP
            final Config scannerConfig = Env.getScannerConfig();

            // Create scanner
            final Scanner scanner = ScannerFactory.createScanner(scannerConfig);

            // Prepare resources
            final ScanJobDAO scanJobDAO = new MySqlScanJobDAO(dataSource);
            final ScanPortResultDAO scanResultDAO = new MySqlScanPortResultDAO(dataSource);

            // Parse target, create and run scan jobs, parse result before returning JSON
            List<CompletableFuture<Job>> scanPortResultFutureList = Stream.of(StringUtils.split(targets, "|;, "))
                    .parallel()
                    .distinct()
                    .limit(Env.getConcurrentScanLimit())
                    .map(target -> CompletableFuture.supplyAsync(() -> scanJobDAO.create(target, scannerConfig.getFullCommandLine(target))))
                    .map(job -> job.thenApplyAsync(scanner::scan))
                    .map(job -> job.thenApplyAsync(NMapXmlScanPortResultParser::parse))
                    .collect(Collectors.<CompletableFuture<Job>>toList());

            // Wait till all job completes to obtain list of job results
            List<Job> completedJobList = FutureUtils.sequence(scanPortResultFutureList).get();

            // Persist the scan port results and update scan job asynchronously
            completedJobList.parallelStream().forEach(completedJob -> CompletableFuture.runAsync(() -> {
                scanResultDAO.create(completedJob.getId(), completedJob.getResult());
                scanJobDAO.update(completedJob);
            }));

            System.out.println("Request is completed in: " + (System.currentTimeMillis() - startTime) + "ms");
            return completedJobList;
        }, new JsonTransformer());
    }
}