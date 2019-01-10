package dk.kb.ccs.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StreamUtilsTest {
    String content = "THIS \n IS \n 3 lines";

    @Test
    public void testInputStreamAsLines() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        List<String> lines = StreamUtils.extractInputStreamAsLines(in);
        
        Assert.assertEquals(lines.size(), 3);
    }
    
    @Test
    public void testInputStreamAsString() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        String text = StreamUtils.extractInputStreamAsString(in);
        
        Assert.assertEquals(text, content + "\n");
    }
}
