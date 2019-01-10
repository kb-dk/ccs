package dk.kb.ccs.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dk.kb.ccs.TestUtils;
import dk.kb.ccs.conf.Configuration;
import dk.kb.ccs.utils.StreamUtils;

@SpringBootTest
public class ReporterTest {

    Configuration conf;
    File dir;
    
    @Before
    public void setup() {
        conf = TestUtils.getTestConfiguration();
        dir = TestUtils.getTmpDir();
    }
    
    @After
    public void tearDown() throws Exception {
        TestUtils.cleanTmpDir();
    }
    
    @Test
    public void test() throws Exception {
        File reportFile = new File(dir, UUID.randomUUID().toString());
        Reporter reporter = new Reporter();
        reporter.conf = conf;
        reporter.reportFile = reportFile;
        
        Assert.assertFalse(reportFile.exists());
        
        Date d = new Date();
        
        // Test with a negative result, no file created
        reporter.addResult(d, -1L);
        Assert.assertFalse(reportFile.exists());
        
        // Test with a result of 1000, create file with single line entry. And that it is now (within 1 sec).
        reporter.addResult(d, 1000L);
        Assert.assertTrue(reportFile.exists());
        List<String> linesInFile = StreamUtils.extractInputStreamAsLines(new FileInputStream(reportFile));
        Assert.assertEquals(linesInFile.size(), 1);
        Long actualDate = Long.decode(linesInFile.get(0).split(Reporter.SEPARATOR)[0]);
        Assert.assertEquals(actualDate.longValue(), d.getTime());
        
        Date d2 = new Date(d.getTime() + 2);
        reporter.addResult(d2, 123L);
        Date d3 = new Date(d.getTime() + 3);
        reporter.addResult(d3, 123456L);
        Date d4 = new Date(d.getTime() + 4);
        reporter.addResult(d4, 6789L);
        Date d5 = new Date(d.getTime() + 5);
        reporter.addResult(d5, 258L);
        linesInFile = StreamUtils.extractInputStreamAsLines(new FileInputStream(reportFile));
        Assert.assertEquals(5, linesInFile.size());

        // Test extracting all entries
        List<BackflowEntry> entries = reporter.getEntriesForInterval(null, null);
        Assert.assertEquals(5, entries.size());
        
        // Test extracting when given a wrong interval
        entries = reporter.getEntriesForInterval(new Date(System.currentTimeMillis() + 2000), new Date(System.currentTimeMillis() + 3000));
        Assert.assertEquals(0, entries.size());

        // Test extracting when newer than first entry
        entries = reporter.getEntriesForInterval(new Date(actualDate+1), new Date(System.currentTimeMillis() + 1000));
        Assert.assertEquals(4, entries.size());
        
        long l = reporter.getSummary(null, null);
        Assert.assertEquals(131626L, l);
        
        MailReport report = reporter.getReport(new Date(0), new Date(System.currentTimeMillis() + 1000));
        Assert.assertTrue(report.getMailBodyContent().contains("131626"));
    }
}
