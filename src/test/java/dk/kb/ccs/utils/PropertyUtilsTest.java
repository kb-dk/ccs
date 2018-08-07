package dk.kb.ccs.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PropertyUtilsTest {

    @Test
    public void testVersion() {
        Assert.assertEquals(PropertyUtils.getVersion(), PropertyUtils.UNDETERMINED_VERSION);
    }
}
