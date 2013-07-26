package com.aurozhkov.alarm.beans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.BitSet;

public class AlarmDaysTest {

    private Method mIntToBitSetMethod;
    private Method mBitSetToIntMethod;

    @Before
    public void setUp() throws Exception {
        mIntToBitSetMethod = AlarmDays.class.getDeclaredMethod("intToBitSet", int.class);
        mIntToBitSetMethod.setAccessible(true);

        mBitSetToIntMethod = AlarmDays.class.getDeclaredMethod("bitSetToInt", BitSet.class);
        mBitSetToIntMethod.setAccessible(true);
    }

    @Test
    public void testSelectedDays() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        alarmDays.select(1, true);
        alarmDays.select(3, true);

        final boolean isSelected = alarmDays.isSelected(1) && alarmDays.isSelected(3);

        Assert.assertTrue(isSelected);
    }

    @Test
    public void testNotSelectedDays() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        alarmDays.select(1, true);
        alarmDays.select(3, true);

        final boolean isNotSelected = !alarmDays.isSelected(0) && !alarmDays.isSelected(2) &&
                !alarmDays.isSelected(4) && !alarmDays.isSelected(5) && !alarmDays.isSelected(6);

        Assert.assertTrue(isNotSelected);
    }

    @Test
    public void testIntToBitSetAll() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        final BitSet actual = (BitSet) mIntToBitSetMethod.invoke(alarmDays, 6543210);

        final BitSet expected = new BitSet(7);
        expected.set(0, 7);

        Assert.assertTrue(actual.equals(expected));
    }

    @Test
    public void testIntToBitSetNothing() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        final BitSet actual = (BitSet) mIntToBitSetMethod.invoke(alarmDays, 0);

        final BitSet expected = new BitSet(7);

        Assert.assertTrue(actual.equals(expected));
    }

    @Test
    public void testIntToBitSetSome() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        final BitSet actual = (BitSet) mIntToBitSetMethod.invoke(alarmDays, 521);

        final BitSet expected = new BitSet(7);
        expected.set(1);
        expected.set(2);
        expected.set(5);

        Assert.assertTrue(actual.equals(expected));
    }

    @Test
    public void testBitSetToIntAll() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        final BitSet bitSet = new BitSet();
        bitSet.set(0, 7);

        final Integer expected = (Integer) mBitSetToIntMethod.invoke(alarmDays, bitSet);

        Assert.assertEquals(expected.intValue(), 6543210);
    }

    @Test
    public void testBitSetToIntNothing() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        final BitSet bitSet = new BitSet();

        final Integer expected = (Integer) mBitSetToIntMethod.invoke(alarmDays, bitSet);

        Assert.assertEquals(expected.intValue(), 0);
    }

    @Test
    public void testBitSetToIntSome() throws Exception {
        final AlarmDays alarmDays = new AlarmDays();
        final BitSet bitSet = new BitSet();
        bitSet.set(3);
        bitSet.set(6);

        final Integer expected = (Integer) mBitSetToIntMethod.invoke(alarmDays, bitSet);

        Assert.assertEquals(expected.intValue(), 63);
    }
}
