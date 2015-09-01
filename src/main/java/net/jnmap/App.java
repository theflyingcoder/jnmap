package net.jnmap;

import net.jnmap.data.dao.mysql.MySqlScanJobDAO;
import net.jnmap.data.dao.mysql.MySqlScanPortResultDAO;
import net.jnmap.parser.NMapXmlScanPortResultParser;
import net.jnmap.scanner.Scanner;
import net.jnmap.scanner.ScannerFactory;

import javax.sql.DataSource;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

/**
 * Main class to instantiate spark web framework
 * <p/>
 * Created by lucas.
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
                        ScannerFactory.createScanner(Env.scannerConfig()),
                        new MySqlScanJobDAO(dataSource),
                        new MySqlScanPortResultDAO(dataSource),
                        new NMapXmlScanPortResultParser()),
                Env.maxScanReportDayCountPerRequest(),
                Env.maxConcurrentScanPerRequest(),
                Env.maxTargetHistoryCountPerRequest());
    }
}