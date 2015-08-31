package net.jnmap.parser;

import net.jnmap.scanner.Job;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * NMap result parser using SAX
 * <p/>
 * Created by lucas on 8/28/15.
 */
public class NMapXmlScanPortResultParser implements ScanPortResultParser {


    public NMapXmlScanPortResultParser() {
    }

    /**
     * Parses the ScanJob.getOutputs() into port information.
     *
     * @param job
     * @return
     */
    public Job parse(Job job) {
        NMapXmlXMLOutputHandler handler = new NMapXmlXMLOutputHandler(job);
        if (job == null) {
            return null;
        }
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            StringReader reader = new StringReader(job.getOutputs());
            InputSource source = new InputSource(reader);
            saxParser.parse(source, handler);
        } catch (ParserConfigurationException | SAXException e) {
            throw new NMapResultParsingException("Failed to create SAX Parser", job.getOutputs(), e);
        } catch (IOException e) {
            throw new NMapResultParsingException("Failed to read XML input source", job.getOutputs(), e);
        }
        System.out.println("Result[" + job.getTarget() + "] was parsed in " + handler.getParseTime() + "ms");
        return job;
    }

}
