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

import dk.kb.ccs.Configuration;
import dk.kb.ccs.TestUtils;
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
        
        // Test with a result of 0, no file created
        reporter.addResult(0L);
        Assert.assertFalse(reportFile.exists());
        
        // Test with a result of 1000, create file with single line entry. And that it is now (within 1 sec).
        Long expectedDate = System.currentTimeMillis();
        reporter.addResult(1000L);
        Assert.assertTrue(reportFile.exists());
        List<String> linesInFile = StreamUtils.extractInputStreamAsLines(new FileInputStream(reportFile));
        Assert.assertEquals(linesInFile.size(), 1);
        Long actualDate = Long.decode(linesInFile.get(0).split(Reporter.SEPARATOR)[0]);
        Assert.assertTrue(actualDate >= expectedDate);
        Assert.assertTrue(actualDate <= expectedDate+1000);
        
        // Test with several more results.
        synchronized(this) {
            wait(100);
        }
        reporter.addResult(123L);
        synchronized(this) {
            wait(100);
        }
        reporter.addResult(123456L);
        synchronized(this) {
            wait(100);
        }
        reporter.addResult(6789L);
        synchronized(this) {
            wait(100);
        }
        reporter.addResult(258L);
        linesInFile = StreamUtils.extractInputStreamAsLines(new FileInputStream(reportFile));
        Assert.assertEquals(linesInFile.size(), 5);

        // Test extracting all entries
        Map<Long, Long> entries = reporter.getEntriesForInterval(null, null);
        Assert.assertEquals(5, entries.size());
        
        // Test extracting when given a wrong interval
        entries = reporter.getEntriesForInterval(new Date(), new Date(System.currentTimeMillis() + 1000));
        Assert.assertEquals(0, entries.size());

        // Test extracting when newer than first entry
        entries = reporter.getEntriesForInterval(new Date(actualDate+1), new Date());
        Assert.assertEquals(entries.size(), 4);
        
    }
}
