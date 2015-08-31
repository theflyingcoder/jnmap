package net.jnmap;

import net.jnmap.util.JsonTransformer;
import org.apache.commons.lang3.StringUtils;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Resource to setup end points
 * <p/>
 * Created by lucas on 8/30/15.
 */
public class ScannerResource {

    private final ScannerService scannerService;
    private final int maxReportDayCount;
    private final int maxConcurrentScan;

    private final int MAX_TARGET_LENGTH = 255;
    private final int maxTargetCount;

    public ScannerResource(ScannerService scannerService, int maxReportDayCount, int maxConcurrentScan, int maxTargetCount) {
        this.scannerService = scannerService;
        this.maxReportDayCount = maxReportDayCount;
        this.maxConcurrentScan = maxConcurrentScan;
        this.maxTargetCount = maxTargetCount;

        setupEndpoints();
    }

    private void setupEndpoints() {
        // POST Handler
        // Scans the given delimited targets
        post("/scan/:targets", "application/json", (request, response) -> {
            String targets = request.params(":targets");
            int targetsLength = targets.length();
            targets = targets.substring(0, Math.min(MAX_TARGET_LENGTH, targetsLength));
            return scannerService.doScan(targets, maxConcurrentScan);
        }, new JsonTransformer());


        // GET Handler
        // Retrieves the scan results and history of the given targets
        get("/scan/:target", "application/json", (request, response) -> {
            final String target = request.params(":target");
            String daysStr = request.params("days");

            int days;
            if (StringUtils.isNumeric(daysStr)) {
                days = Integer.parseInt(daysStr);
            } else {
                days = maxReportDayCount;
            }
            int reportDaysToBeUsed = Math.min(maxReportDayCount, days);
            return scannerService.getScanResults(target, reportDaysToBeUsed, maxTargetCount);
        }, new JsonTransformer());
    }
}
