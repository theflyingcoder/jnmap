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
 * Created by lucas on 8/30/15.
 */
public class Env {

    public static final String ENV_CLEARDB_DATABASE_URL = "CLEARDB_DATABASE_URL";
    public static final String ENV_PORT = "PORT";
    public static final String ENV_NMAP_PATH = "NMAP_PATH";
    public static final String ENV_NMAP_OPTIONS = "NMAP_OPTIONS";

    public static final String ENV_LIMIT_CONCURRENT_SCAN = "LIMIT_CONCURRENT_SCAN";
    public static final String ENV_LIMIT_REPORT_DAY = "LIMIT_REPORT_DAY";

    public static final String DEFAULT_LIMIT_CONCURRENT_SCAN = "10";
    public static final String DEFAULT_LIMIT_REPORT_DAY = "3";

    /**
     * Initializes data source
     *
     * @return
     */
    static DataSource getDataSource() {
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
    static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(ENV_PORT) != null) {
            return Integer.parseInt(processBuilder.environment().get(ENV_PORT));
        }
        return SPARK_DEFAULT_PORT;
    }

    /**
     * Retrieves scan configurations
     *
     * @return
     */
    static Config getScannerConfig() {
        String pathToNMap = System.getenv(ENV_NMAP_PATH);
        String nMapOptions = System.getenv(ENV_NMAP_OPTIONS);
        return new NMapConfig(pathToNMap, nMapOptions);
    }

    static int getConcurrentScanLimit() {
        String limit = StringUtils.defaultString(System.getenv(ENV_LIMIT_CONCURRENT_SCAN), DEFAULT_LIMIT_CONCURRENT_SCAN);
        return Integer.parseInt(limit);
    }

    static int getScanReportDaysLimit() {
        String limit = StringUtils.defaultString(System.getenv(ENV_LIMIT_REPORT_DAY), DEFAULT_LIMIT_REPORT_DAY);
        return Integer.parseInt(limit);
    }
}
