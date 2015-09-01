package net.jnmap;

import net.jnmap.scanner.nmap.NMapConfig;

/**
 * Created by lucas.
 */
public class TestConstants {
    public static final String SAMPLE_XML_OUTPUT1 = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE nmaprun>\n" +
            "<?xml-stylesheet href=\"file:///usr/local/bin/../share/nmap/nmap.xsl\" type=\"text/xsl\"?>\n" +
            "<!-- Nmap 6.47 scan initiated Mon Aug 31 12:20:30 2015 as: nmap -T4 -p 0-1000 -oX - 192.168.1.1 -->\n" +
            "<nmaprun scanner=\"nmap\" args=\"nmap -T4 -p 0-1000 -oX - 192.168.1.1\" start=\"1441048830\" startstr=\"Mon Aug 31 12:20:30 2015\" version=\"6.47\" xmloutputversion=\"1.04\">\n" +
            "    <scaninfo type=\"connect\" protocol=\"tcp\" numservices=\"1001\" services=\"0-1000\"/>\n" +
            "    <verbose level=\"0\"/>\n" +
            "    <debugging level=\"0\"/>\n" +
            "    <host starttime=\"1441048830\" endtime=\"1441048905\">\n" +
            "        <status state=\"up\" reason=\"unknown-response\" reason_ttl=\"0\"/>\n" +
            "        <address addr=\"192.168.1.1\" addrtype=\"ipv4\"/>\n" +
            "        <hostnames>\n" +
            "            <hostname name=\"router.asus.com\" type=\"PTR\"/>\n" +
            "        </hostnames>\n" +
            "        <ports>\n" +
            "            <extraports state=\"filtered\" count=\"502\">\n" +
            "                <extrareasons reason=\"no-responses\" count=\"502\"/>\n" +
            "            </extraports>\n" +
            "            <extraports state=\"closed\" count=\"495\">\n" +
            "                <extrareasons reason=\"conn-refused\" count=\"495\"/>\n" +
            "            </extraports>\n" +
            "            <port protocol=\"tcp\" portid=\"53\">\n" +
            "                <state state=\"open\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"domain\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "            <port protocol=\"tcp\" portid=\"80\">\n" +
            "                <state state=\"closed\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"http\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "            <port protocol=\"tcp\" portid=\"139\">\n" +
            "                <state state=\"filtered\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"netbios-ssn\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "            <port protocol=\"tcp\" portid=\"445\">\n" +
            "                <state state=\"open\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"microsoft-ds\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "        </ports>\n" +
            "        <times srtt=\"9703\" rttvar=\"248\" to=\"100000\"/>\n" +
            "    </host>\n" +
            "    <runstats>\n" +
            "        <finished time=\"1441048905\" timestr=\"Mon Aug 31 12:21:45 2015\" elapsed=\"74.79\" summary=\"Nmap done at Mon Aug 31 12:21:45 2015; 1 IP address (1 host up) scanned in 74.79 seconds\" exit=\"success\"/>\n" +
            "        <hosts up=\"1\" down=\"0\" total=\"1\"/>\n" +
            "    </runstats>\n" +
            "</nmaprun>\n";

    public static final String SAMPLE_XML_OUTPUT2 = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE nmaprun>\n" +
            "<?xml-stylesheet href=\"file:///usr/local/bin/../share/nmap/nmap.xsl\" type=\"text/xsl\"?>\n" +
            "<!-- Nmap 6.47 scan initiated Mon Aug 31 12:20:30 2015 as: nmap -T4 -p 0-1000 -oX - 192.168.1.2 -->\n" +
            "<nmaprun scanner=\"nmap\" args=\"nmap -T4 -p 0-1000 -oX - 192.168.1.2\" start=\"1441048830\" startstr=\"Mon Aug 31 12:20:30 2015\" version=\"6.47\" xmloutputversion=\"1.04\">\n" +
            "    <scaninfo type=\"connect\" protocol=\"tcp\" numservices=\"1001\" services=\"0-1000\"/>\n" +
            "    <verbose level=\"0\"/>\n" +
            "    <debugging level=\"0\"/>\n" +
            "    <host starttime=\"1441048830\" endtime=\"1441048905\">\n" +
            "        <status state=\"up\" reason=\"unknown-response\" reason_ttl=\"0\"/>\n" +
            "        <address addr=\"192.168.1.1\" addrtype=\"ipv4\"/>\n" +
            "        <hostnames>\n" +
            "            <hostname name=\"router.asus.com\" type=\"PTR\"/>\n" +
            "        </hostnames>\n" +
            "        <ports>\n" +
            "            <extraports state=\"filtered\" count=\"502\">\n" +
            "                <extrareasons reason=\"no-responses\" count=\"502\"/>\n" +
            "            </extraports>\n" +
            "            <port protocol=\"tcp\" portid=\"80\">\n" +
            "                <state state=\"closed\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"http\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "            <port protocol=\"tcp\" portid=\"139\">\n" +
            "                <state state=\"filtered\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"netbios-ssn\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "            <port protocol=\"tcp\" portid=\"445\">\n" +
            "                <state state=\"open\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"microsoft-ds\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "        </ports>\n" +
            "        <times srtt=\"9703\" rttvar=\"248\" to=\"100000\"/>\n" +
            "    </host>\n" +
            "    <runstats>\n" +
            "        <finished time=\"1441048905\" timestr=\"Mon Aug 31 12:21:45 2015\" elapsed=\"74.79\" summary=\"Nmap done at Mon Aug 31 12:21:45 2015; 1 IP address (1 host up) scanned in 74.79 seconds\" exit=\"success\"/>\n" +
            "        <hosts up=\"1\" down=\"0\" total=\"1\"/>\n" +
            "    </runstats>\n" +
            "</nmaprun>\n";

    public static final String SAMPLE_XML_OUTPUT3 = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE nmaprun>\n" +
            "<?xml-stylesheet href=\"file:///usr/local/bin/../share/nmap/nmap.xsl\" type=\"text/xsl\"?>\n" +
            "<!-- Nmap 6.47 scan initiated Mon Aug 31 12:20:30 2015 as: nmap -T4 -p 0-1000 -oX - 192.168.1.3 -->\n" +
            "<nmaprun scanner=\"nmap\" args=\"nmap -T4 -p 0-1000 -oX - 192.168.1.3\" start=\"1441048830\" startstr=\"Mon Aug 31 12:20:30 2015\" version=\"6.47\" xmloutputversion=\"1.04\">\n" +
            "    <scaninfo type=\"connect\" protocol=\"tcp\" numservices=\"1001\" services=\"0-1000\"/>\n" +
            "    <verbose level=\"0\"/>\n" +
            "    <debugging level=\"0\"/>\n" +
            "    <host starttime=\"1441048830\" endtime=\"1441048905\">\n" +
            "        <status state=\"up\" reason=\"unknown-response\" reason_ttl=\"0\"/>\n" +
            "        <address addr=\"192.168.1.1\" addrtype=\"ipv4\"/>\n" +
            "        <hostnames>\n" +
            "            <hostname name=\"router.asus.com\" type=\"PTR\"/>\n" +
            "        </hostnames>\n" +
            "        <ports>\n" +
            "            <extraports state=\"filtered\" count=\"502\">\n" +
            "                <extrareasons reason=\"no-responses\" count=\"502\"/>\n" +
            "            </extraports>\n" +
            "            <port protocol=\"tcp\" portid=\"139\">\n" +
            "                <state state=\"filtered\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"netbios-ssn\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "            <port protocol=\"tcp\" portid=\"445\">\n" +
            "                <state state=\"open\" reason=\"syn-ack\" reason_ttl=\"0\"/>\n" +
            "                <service name=\"microsoft-ds\" method=\"table\" conf=\"3\"/>\n" +
            "            </port>\n" +
            "        </ports>\n" +
            "        <times srtt=\"9703\" rttvar=\"248\" to=\"100000\"/>\n" +
            "    </host>\n" +
            "    <runstats>\n" +
            "        <finished time=\"1441048905\" timestr=\"Mon Aug 31 12:21:45 2015\" elapsed=\"74.79\" summary=\"Nmap done at Mon Aug 31 12:21:45 2015; 1 IP address (1 host up) scanned in 74.79 seconds\" exit=\"success\"/>\n" +
            "        <hosts up=\"1\" down=\"0\" total=\"1\"/>\n" +
            "    </runstats>\n" +
            "</nmaprun>\n";

    public static final String SAMPLE_XML_ERROR = "Strange error from connect (49):Can't assign requested address";
    public static final String TEST_TARGET = "testTarget";
    public static final String TEST_TARGET_STATUS_UP = "up";
    public static final String TEST_TARGET_STATUS_DOWN = "down";
    public static final NMapConfig TEST_CONFIG = new NMapConfig("nmap", "option");
    public static final String TEST_COMMAND_PREFIX = TEST_CONFIG.getCommandLinePrefix();
    public static final long TEST_JOB_ID = 23L;
    public static final long TEST_JOB_ID1 = 24L;
    public static final long TEST_JOB_ID2 = 25L;
    public static final float TEST_ELAPSED = 88.23f;
}
