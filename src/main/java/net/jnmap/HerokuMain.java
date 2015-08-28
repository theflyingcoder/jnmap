package net.jnmap;

import net.jnmap.nmap.Executor;

import static spark.Spark.*;

/**
 * Created by lhalim on 8/27/15.
 */
public class HerokuMain {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/", (req, res) -> new Executor(null).execute());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}