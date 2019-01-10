package dk.kb.ccs.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StringUtilsTest {

    String line1 = "line 1 " + UUID.randomUUID().toString();
    String line2 = "line 2 " + UUID.randomUUID().toString();
    String line3 = "line 3 " + UUID.randomUUID().toString();
    
    @Test
    public void testListToString() {
        List<String> lines = Arrays.asList(line1, line2, line3);
        String text = StringUtils.listToString(lines, "\n");
        
        Assert.assertEquals(text.split("\n").length, 3);
        Assert.assertTrue(text.contains(line1));
        Assert.assertTrue(text.contains(line2));
        Assert.assertTrue(text.contains(line3));
        
        Assert.assertEquals(StringUtils.listToString(null, "\n"), StringUtils.EMPTY_LIST);
        Assert.assertEquals(StringUtils.listToString(new ArrayList<String>(), "\n"), StringUtils.EMPTY_LIST);
    }
}
