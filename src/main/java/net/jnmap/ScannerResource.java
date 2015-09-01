package net.jnmap;

import net.jnmap.util.JsonTransformer;
import net.jnmap.util.ValidatorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import spark.QueryParamsMap;
import spark.Response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Resource to setup end points
 * <p/>
 * Created by lucas.
 */
public class ScannerResource {

    private final ScannerService scannerService;
    private final int maxReportDayCount;
    private final int maxConcurrentScan;

    private final int MAX_TARGET_LENGTH = 255;
    private final int maxTargetCount;

    final static String ACCEPTED_DELIM = "|;, ";
    final static String MSG_SCAN_FAILED_INVALID_TARGET = "Scan failed: invalid target(s)";

    public ScannerResource(ScannerService scannerService,
                           int maxReportDayCount,
                           int maxConcurrentScan,
                           int maxTargetCount) {
        this.scannerService = scannerService;
        this.maxReportDayCount = maxReportDayCount;
        this.maxConcurrentScan = maxConcurrentScan;
        this.maxTargetCount = maxTargetCount;

        setupEndpoints();
    }

    /**
     * Setup end points
     */
    private void setupEndpoints() {

        // POST Handler
        // Scans the given delimited targets
        post("/scan/:targets", "application/json", (request, response) -> {
            String targets = request.params(":targets");
            int targetsLength = targets.length();
            // Prevent crazy string sizes
            targets = targets.substring(0, Math.min(MAX_TARGET_LENGTH, targetsLength));

            // Check for invalid target, if there is just say invalid target
            String[] targetArray = StringUtils.split(targets, ACCEPTED_DELIM);
            List<String> invalidTarget = Stream.of(targetArray)
                    .filter(target -> !ValidatorUtils.isValidTarget(target))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(invalidTarget)) {
                response.status(403);
                return MSG_SCAN_FAILED_INVALID_TARGET;
            }
            return scannerService.doScan(targetArray, maxConcurrentScan);
        }, new JsonTransformer());


        // GET Handler
        // Retrieves the scan results and history of the given targets
        get("/scan/:targets", "application/json", (request, response) -> {
            final String targets = request.params(":targets");
            String[] targetArray = StringUtils.split(targets, ACCEPTED_DELIM);
            List<String> invalidTarget = Stream.of(targetArray)
                    .filter(target -> !ValidatorUtils.isValidTarget(target))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(invalidTarget)) {
                response.status(403);
                return MSG_SCAN_FAILED_INVALID_TARGET;
            }

            QueryParamsMap dayQuery = request.queryMap("days");
            String daysStr = StringUtils.EMPTY;
            if (null != dayQuery) {
                daysStr = dayQuery.value();
            }
            int days;
            if (StringUtils.isNumeric(daysStr)) {
                days = Integer.parseInt(daysStr);
            } else {
                days = maxReportDayCount;
            }
            int reportDaysToBeUsed = Math.min(maxReportDayCount, days);
            return scannerService.getScanResults(targetArray, reportDaysToBeUsed, maxTargetCount);
        }, new JsonTransformer());
    }

}
