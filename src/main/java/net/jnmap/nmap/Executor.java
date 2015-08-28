package net.jnmap.nmap;


import net.jnmap.utils.Converter;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import spark.utils.IOUtils;

import java.io.IOException;

/**
 * Created by xephyr on 8/27/15.
 */
public class Executor {


    public Executor(String[] hosts) {

    }

    public String execute() throws ExecutionException {
        String result = new String();
        try {
            Process nmapProcess = Runtime.getRuntime().exec("~/bin/nmap -T4 -A -p 1-1000 -oX - scanme.nmap.org");

            String xml = IOUtils.toString(nmapProcess.getInputStream());
            JSON objJson = new XMLSerializer().read(xml);
//            result.setOutputs(Converter.inputStreamToString(nmapProcess.getInputStream()));
//            result.setErrors(Converter.inputStreamToString(nmapProcess.getErrorStream()));
            result = objJson.toString();

        } catch (IOException e) {
            throw new ExecutionException(e.getMessage(), e);
        }
        return result;
    }

}
