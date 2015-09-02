package net.jnmap.parser;

import net.jnmap.data.Port;
import net.jnmap.data.ScanPortResult;
import net.jnmap.scanner.Job;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * SAX Parser Handler to create NMapRun object from XML
 * <p/>
 * Created by lucas.
 */
public class NMapXmlXMLOutputHandler extends DefaultHandler {

    private Job job;

    public NMapXmlXMLOutputHandler(Job job) {
        this.job = job;
    }

    private long startParseTime;
    private long endParseTime;

    /**
     * Handles of documents
     */
    @Override
    public void startDocument() {
        startParseTime = System.currentTimeMillis();
    }

    /**
     * Handle start of elements
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws org.xml.sax.SAXException {
        if (ScanPortResult.TAG_NMAPRUN.equals(qName)) {
            job.setResult(new ScanPortResult());
        } else if (Port.TAG_PORT.equals(qName)) {
            ScanPortResult scanPortResult = (ScanPortResult) job.getResult();
            Port port = new Port();
            port.setPort(Integer.valueOf(attributes.getValue(Port.ATTR_PORT_ID)));
            port.setProtocol(attributes.getValue(Port.ATTR_PROTOCOL));
            scanPortResult.addPort(port);
        } else if (Port.TAG_SERVICE_STATE.equals(qName)) {
            ScanPortResult scanPortResult = (ScanPortResult) job.getResult();
            List<Port> ports = scanPortResult.getPorts();
            Port port = ports.get(ports.size() - 1);
            port.setState(attributes.getValue(Port.ATTR_STATE));
        } else if (Port.TAG_PORT_SERVICE.equals(qName)) {
            ScanPortResult scanPortResult = (ScanPortResult) job.getResult();
            List<Port> ports = scanPortResult.getPorts();
            ports.get(ports.size() - 1).setService(attributes.getValue(Port.ATTR_SERVICE_NAME));
        } else if (Job.TAG_HOST_STATUS.equals(qName)) {
            job.setTargetStatus(attributes.getValue(Job.ATTR_HOST_STATUS_STATE));
        } else if (Job.TAG_FINISHED.equals(qName)) {
            Float elapsed = Float.parseFloat(attributes.getValue(Job.ATTR_ELAPSED));
            job.setElapsedSecs(elapsed);
        }
    }

    /**
     * Handle the end of document
     *
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        endParseTime = System.currentTimeMillis();
    }

    /**
     * Returns parsing time
     *
     * @return
     */
    public long getParseTime() {
        return endParseTime - startParseTime;
    }

}
