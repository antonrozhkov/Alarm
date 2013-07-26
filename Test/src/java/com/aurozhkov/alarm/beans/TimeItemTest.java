package com.aurozhkov.alarm.beans;


import org.junit.Assert;
import org.junit.Test;

public class TimeItemTest {

    @Test
    public void testToInt() {
        final TimeItem timeItem = new TimeItem(16, 26);
        final int expected = timeItem.toInt();

        Assert.assertEquals(1626, expected);
    }

    @Test
    public void fromInt() {
        final TimeItem expected = TimeItem.fromInt(1905);

        Assert.assertEquals(19, expected.hours);
        Assert.assertEquals(5, expected.minutes);
    }
}
