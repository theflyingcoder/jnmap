package net.jnmap.parser;

import net.jnmap.TestConstants;
import net.jnmap.data.Port;
import net.jnmap.data.ScanJob;
import net.jnmap.scanner.Job;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lucas on 8/31/15.
 */
public class NMapXmlScanPortResultParserTest {

    @Test
    public void testParse() {
        NMapXmlScanPortResultParser parser = new NMapXmlScanPortResultParser();
        Job job = new ScanJob();
        job.setOutputs(TestConstants.SAMPLE_XML_OUTPUT1);
        job.setErrors("Strange error from connect (49):Can't assign requested address");
        job = parser.parse(job);

        Assert.assertEquals(4, job.getResult().getPorts().size());
        Assert.assertEquals(74.79f, job.getElapsedSecs(), 0);
        Assert.assertEquals("up", job.getTargetStatus());

        int[] expectedPortNos = {53,80,139,445};
        String[] expectedProtocols = {"tcp", "tcp", "tcp", "tcp"};
        String[] expectedStates = {"open", "closed", "filtered", "open"};
        String[] expectedServices = {"domain", "http", "netbios-ssn", "microsoft-ds"};
        int idx = 0;
        for (Port port : job.getResult().getPorts()) {
            Assert.assertEquals(expectedPortNos[idx], port.getPort());
            Assert.assertEquals(expectedStates[idx], port.getState());
            Assert.assertEquals(expectedServices[idx], port.getService());
            Assert.assertEquals(expectedProtocols[idx], port.getProtocol());
            idx++;
        }

    }
}
