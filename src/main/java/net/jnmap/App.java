package net.jnmap;

import net.jnmap.data.dao.mysql.MySqlScanJobDAO;
import net.jnmap.data.dao.mysql.MySqlScanPortResultDAO;
import net.jnmap.parser.NMapXmlScanPortResultParser;

import javax.sql.DataSource;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

/**
 * Main class to instantiate spark web framework
 * <p/>
 * Created by lhalim on 8/27/15.
 */
public class App {

    /**
     * Controller method where we do all the spark setup the routes and necessary resources.
     *
     * @param args
     */
    public static void main(String[] args) {
        port(Env.assignedPort());
        staticFileLocation("/public");

        // Initialize data source
        final DataSource dataSource = Env.dataSource();

        new ScannerResource(
                new ScannerService(
                        Env.scannerConfig(),
                        new MySqlScanJobDAO(dataSource),
                        new MySqlScanPortResultDAO(dataSource),
                        new NMapXmlScanPortResultParser()),
                Env.maxScanReportDayCount(),
                Env.maxConcurrentScan(),
                Env.maxTargetHistoryCount());
    }
}