package net.jnmap;

import net.jnmap.data.ScanJob;
import net.jnmap.data.dao.ScanJobDAO;
import net.jnmap.data.dao.ScanPortResultDAO;
import net.jnmap.parser.NMapXmlScanPortResultParser;
import net.jnmap.parser.ScanPortResultParser;
import net.jnmap.scanner.Job;
import net.jnmap.scanner.Scanner;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by lucas.
 */
public class ScannerServiceTest {

    private Scanner scanner;
    private ScanJobDAO scanJobDAO;
    private ScanPortResultDAO scanResultDAO;
    private ScanPortResultParser scanPortResultParser;
    private String[] targets = {"target1", "target2", "target3"};


    @Before
    public void setUp() {
        this.scanner = mock(Scanner.class);
        this.scanJobDAO = mock(ScanJobDAO.class);
        this.scanResultDAO = mock(ScanPortResultDAO.class);
        this.scanPortResultParser = mock(ScanPortResultParser.class);
    }

    @Test
    public void testGetScanResults() {
        ScannerService scannerService = new ScannerService(scanner, scanJobDAO, scanResultDAO, scanPortResultParser);
        ScanJob job1 = new ScanJob(targets[0]);
        ScanJob job2 = new ScanJob(targets[0]);
        ScanJob job3 = new ScanJob(targets[1]);
        ScanJob job4 = new ScanJob(targets[2]);

        List<Job> allJobs = Arrays.asList(job1, job2, job3, job4);
        List<Job> jobList1 = Arrays.asList(job1, job2);
        List<Job> jobList2 = Collections.singletonList(job3);
        List<Job> jobList3 = Collections.singletonList(job4);

        final int maxReportDayCountPerReq = 10;

        when(scanJobDAO.get(targets[0], maxReportDayCountPerReq)).thenReturn(jobList1);
        when(scanJobDAO.get(targets[1], maxReportDayCountPerReq)).thenReturn(jobList2);
        when(scanJobDAO.get(targets[2], maxReportDayCountPerReq)).thenReturn(jobList3);

        List<Job> returnedJobs = scannerService.getScanResults(targets, maxReportDayCountPerReq, 5);
        assertTrue(returnedJobs.containsAll(allJobs));

        returnedJobs = scannerService.getScanResults(targets, maxReportDayCountPerReq, 2);
        assertEquals(3, returnedJobs.size());
    }

    @Test
    public void testDoScan() throws ExecutionException, InterruptedException {
        ScannerService scannerService = new ScannerService(scanner, scanJobDAO, scanResultDAO, scanPortResultParser);
        Integer[] jobIds = {1,2,3};


        ScanJob job1 = new ScanJob(targets[0]);
        ScanJob job2 = new ScanJob(targets[1]);
        ScanJob job3 = new ScanJob(targets[2]);

        job1.setId(jobIds[0]);
        job2.setId(jobIds[1]);
        job3.setId(jobIds[2]);

        job1.setOutputs(TestConstants.SAMPLE_XML_OUTPUT1.trim());
        job2.setOutputs(TestConstants.SAMPLE_XML_OUTPUT2.trim());
        job3.setOutputs(TestConstants.SAMPLE_XML_OUTPUT3.trim());

        Job parsedJob1 = new NMapXmlScanPortResultParser().parse(job1);
        Job parsedJob2 = new NMapXmlScanPortResultParser().parse(job2);
        Job parsedJob3 = new NMapXmlScanPortResultParser().parse(job3);

        when(scanJobDAO.create(targets[0], scanner.getFullCommandLine(targets[0]))).thenReturn(job1);
        when(scanJobDAO.create(targets[1], scanner.getFullCommandLine(targets[1]))).thenReturn(job2);
        when(scanJobDAO.create(targets[2], scanner.getFullCommandLine(targets[2]))).thenReturn(job3);

        when(scanner.scan(job1)).thenReturn(job1);
        when(scanner.scan(job2)).thenReturn(job2);
        when(scanner.scan(job3)).thenReturn(job3);

        when(scanPortResultParser.parse(job1)).thenReturn(parsedJob1);
        when(scanPortResultParser.parse(job2)).thenReturn(parsedJob2);
        when(scanPortResultParser.parse(job3)).thenReturn(parsedJob3);

        List<Job> returnedJobList = scannerService.doScan(targets, 5);
        assertEquals(3, returnedJobList.size());

        // Job 1 should produce 4 results
        assertEquals(4, returnedJobList.stream().filter(job -> targets[0].equals(job.getTarget())).collect(Collectors.toList()).get(0).getResult().getPorts().size());

        // Job 2 should produce 3 results
        assertEquals(3, returnedJobList.stream().filter(job -> targets[1].equals(job.getTarget())).collect(Collectors.toList()).get(0).getResult().getPorts().size());

        // Job 3 should produce 3 results
        assertEquals(2, returnedJobList.stream().filter(job -> targets[2].equals(job.getTarget())).collect(Collectors.toList()).get(0).getResult().getPorts().size());


        // If max concurrent scan reduced to 2, it should only return 2 results instead of 3
        returnedJobList = scannerService.doScan(targets, 2);
        assertEquals(2, returnedJobList.size());

        returnedJobList = scannerService.doScan(targets, 0);
        assertEquals(0, returnedJobList.size());
    }
}
