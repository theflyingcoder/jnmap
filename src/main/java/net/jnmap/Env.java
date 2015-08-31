package net.jnmap;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import net.jnmap.scanner.Config;
import net.jnmap.scanner.nmap.NMapConfig;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

import static spark.SparkBase.SPARK_DEFAULT_PORT;

/**
 * Environmental configurations
 *
 * Created by lucas on 8/30/15.
 */
public class Env {

    public static final String ENV_CLEARDB_DATABASE_URL = "CLEARDB_DATABASE_URL";
    public static final String ENV_PORT = "PORT";
    public static final String ENV_NMAP_PATH = "NMAP_PATH";
    public static final String ENV_NMAP_OPTIONS = "NMAP_OPTIONS";
    public static final String ENV_MAX_CONCURRENT_SCAN = "MAX_CONCURRENT_SCAN";
    public static final String ENV_MAX_TARGET_HISTORY_COUNT = "MAX_TARGET_HISTORY_COUNT";
    public static final String ENV_MAX_REPORT_DAY = "MAX_REPORT_DAY";

    public static final String DEFAULT_MAX_CONCURRENT_SCAN = "10";
    public static final String DEFAULT_MAX_REPORT_DAY = "3";
    public static final String DEFAULT_MAX_TARGET_HISTORY_COUNT = "10";

    /**
     * Initializes and supply datasource object
     *
     * @return
     */
    static DataSource dataSource() {
        String mySqlDbUrl = System.getenv(ENV_CLEARDB_DATABASE_URL);
        if (StringUtils.isEmpty(mySqlDbUrl)) {
            System.err.println("Please specify full mysql url using environment var $" + ENV_CLEARDB_DATABASE_URL);
            System.exit(1);
        }
        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv(ENV_CLEARDB_DATABASE_URL));
        } catch (URISyntaxException e) {
            System.err.println("Failed creating data source for URI");
            e.printStackTrace();
            System.exit(1);
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(dbUrl);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    /**
     * Retrieves configured port number for starting spark web server
     *
     * @return
     */
    static int assignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(ENV_PORT) != null) {
            return Integer.parseInt(processBuilder.environment().get(ENV_PORT));
        }
        return SPARK_DEFAULT_PORT;
    }

    /**
     * Returns scan configurations based on environment variables
     *
     * @return
     */
    static Config scannerConfig() {
        String pathToNMap = System.getenv(ENV_NMAP_PATH);
        String nMapOptions = System.getenv(ENV_NMAP_OPTIONS);
        return new NMapConfig(pathToNMap, nMapOptions);
    }

    /**
     * Returns concurrent scan limit based on environment variables if found, otherwise default value
     *
     * @return
     */
    static int maxConcurrentScan() {
        String limit = StringUtils.defaultString(System.getenv(ENV_MAX_TARGET_HISTORY_COUNT), DEFAULT_MAX_CONCURRENT_SCAN);
        return Integer.parseInt(limit);
    }

    /**
     * Returns concurrent scan limit based on environment variables if found, otherwise default value
     *
     * @return
     */
    static int maxTargetHistoryCount() {
        String limit = StringUtils.defaultString(System.getenv(ENV_MAX_CONCURRENT_SCAN), DEFAULT_MAX_TARGET_HISTORY_COUNT);
        return Integer.parseInt(limit);
    }

    /**
     * Returns Ma
     *
     * @return
     */
    static int maxScanReportDayCount() {
        String limit = StringUtils.defaultString(System.getenv(ENV_MAX_REPORT_DAY), DEFAULT_MAX_REPORT_DAY);
        return Integer.parseInt(limit);
    }
}
