package net.jnmap.scanner.nmap;

import net.jnmap.data.ScanJob;
import net.jnmap.scanner.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static net.jnmap.TestConstants.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by lucas.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(NMapScanner.class)
public class NMapScannerTest {
    @Test
    public void testScan() throws IOException {

        mockStatic(Runtime.class);
        Runtime mockedRuntime = mock(Runtime.class);
        Process mockedProcess = mock(Process.class);
        // Mock streams with real strings
        InputStream mockedInputStream = new ByteArrayInputStream(SAMPLE_XML_OUTPUT1.getBytes());
        InputStream mockedErrorStream = new ByteArrayInputStream(SAMPLE_XML_ERROR.getBytes());

        // Setups before executing scans
        ScanJob testJob = new ScanJob(TEST_TARGET);
        when(Runtime.getRuntime()).thenReturn(mockedRuntime);
        when(mockedProcess.getInputStream()).thenReturn(mockedInputStream);
        when(mockedProcess.getErrorStream()).thenReturn(mockedErrorStream);

        // Execute scans and expect the output/errors
        NMapScanner scanner = new NMapScanner(TEST_CONFIG.getCommandLinePrefix());
        when(mockedRuntime.exec(scanner.getFullCommandLine(TEST_TARGET))).thenReturn(mockedProcess);
        Job returnedJob = scanner.scan(testJob);
        assertEquals(SAMPLE_XML_OUTPUT1.trim(), returnedJob.getOutputs().trim());
        assertEquals(SAMPLE_XML_ERROR.trim(), returnedJob.getErrors().trim());

    }
}
