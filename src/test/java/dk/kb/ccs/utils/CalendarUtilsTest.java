package dk.kb.ccs.utils;

import java.util.Date;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.Assert;

@SpringBootTest
public class CalendarUtilsTest {

    @Test
    public void testDateTimeAsString() {
        Assert.assertEquals(CalendarUtils.getDateTimeAsString(new Date(1532690197427L)), "2018-07-27 13:16:37.427+0200");
        Assert.assertEquals(CalendarUtils.getDateTimeAsString(new Date(0L)), "1970-01-01 01:00:00.000+0100");
    }

    @Test
    public void testDateAsString() {
        Assert.assertEquals(CalendarUtils.getDateAsString(new Date(1532690197427L)), "2018-07-27");
        Assert.assertEquals(CalendarUtils.getDateAsString(new Date(0L)), "1970-01-01");
    }
    
    @Test
    public void testDateFromString() {
        Assert.assertEquals(CalendarUtils.getDateFromString("1970-01-01").getTime(), -3600000L);
        Assert.assertEquals(CalendarUtils.getDateFromString("1970/01/01").getTime(), -3600000L);
        Assert.assertEquals(CalendarUtils.getDateFromString("1970-01-01 01:00:00.000+0100").getTime(), 0L);
        Assert.assertEquals(CalendarUtils.getDateFromString("0").getTime(), 0L);

        Assert.assertEquals(CalendarUtils.getDateFromString("2018-07-27").getTime(), 1532642400000L);
        Assert.assertEquals(CalendarUtils.getDateFromString("2018/07/27").getTime(), 1532642400000L);
        Assert.assertEquals(CalendarUtils.getDateFromString("2018-07-27 13:16:37.427+0200").getTime(), 1532690197427L);
        Assert.assertEquals(CalendarUtils.getDateFromString("1532690197427").getTime(), 1532690197427L);
        
        Assert.assertEquals(CalendarUtils.getDateFromString("THIS IS NOT A DATE").getTime(), 0L);
    }
}
