package com.onevour.core;

import android.content.Context;
import android.util.Log;

import com.onevour.core.utilities.beans.BeanCopy;
import com.onevour.core.utilities.beans.SampleBean;
import com.onevour.core.utilities.format.NFormat;
import com.onevour.core.utilities.http.ApiRequest;
import com.onevour.core.utilities.input.InputDouble;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, ApiRequest.class})
public class InputNumberUnitTest {

    @Mock
    Context context;

    @Test
    public void test_copy_bean() throws Exception {
        SampleBean source = new SampleBean();
        source.setName("ZULIADIN");
        source.setLastName("NO LAST NAME");
        source.setBod(new Date());
        SampleBean.Education education = new SampleBean.Education();
        education.setEducation("UBL");
        source.setEducation(education);

        SampleBean.Education educationSD = new SampleBean.Education();
        educationSD.setEducation("SD 01");
        List<SampleBean.Education> educationList = new ArrayList<>();
        educationList.add(educationSD);

        source.setEducations(educationList);

        SampleBean target = new SampleBean();
        BeanCopy.copyValue(source, target, "name", "date", "lastName");
        System.out.println("Result copy: " + target.getLastName() + " | " + target.getBod() + "|" + target.getEducation().getEducation() + "|" + target.getEducations().size());
        for (SampleBean.Education child : target.getEducations()) {
            System.out.println("education is " + child.getEducation());
        }
    }

    @Test
    public void test_print_exponent() throws Exception {
        NumberFormat format = NFormat.currency();
        InputDouble input = new InputDouble(format, 0.0, Double.MAX_VALUE);
        input.append("1", "2", "3", "4", ".", "0", "0", "0", "0", "0", "0", "0", "0");
        input.reset();
        input.append("1", "0", "0", "0", "0", "0", "0", "0", "0", ".", "5", "7");
        input.reset();
        input.append("1", "2", "3", "4", ".", "5", "6");
        input.reset();
        input.append("1", "2", "3", "4", "5", "6", ".", "7", "8");
        input.reset();
        input.append("1", "2", "3", "4", "5", "6", "0", "7", "8", ".", "7", "8");
        input.append("1", "2", "3", "4", "5", "6", "7", "8", "0", "7", "8", "0", "7", "8", ".", "7", "8", "7", "8", "7", "8");
        input.delete();
        input.append("1", "2");
        System.out.println("hello " + input.getValue().toString());
    }

    @Test
    public void test_number_input_double() throws Exception {
        // Assert.assertEquals(0.0010f, 0.0013f, 0.0003f);
        // Above is true because 0.0003 <= (0.0013 - 0.0010 = 0.0003)
        NumberFormat format = NFormat.currency();
        InputDouble input = new InputDouble(format, 0.0, Double.MAX_VALUE);
        input.append("1");
        Assert.assertEquals(1.0, input.getValueDouble(), 0.0);
        input.append("0");
        Assert.assertEquals(10.0, input.getValueDouble(), 0.0);
        input.append(".");
        Assert.assertEquals(10.0, input.getValueDouble(), 0.0);
        input.append("1");
        Assert.assertEquals(10.10, input.getValueDouble(), 0.0);
        input.append("5");
        Assert.assertEquals(10.15, input.getValueDouble(), 0.0);
        input.delete();
        Assert.assertEquals(10.10, input.getValueDouble(), 0.0);
        input.delete();
        Assert.assertEquals(10.0, input.getValueDouble(), 0.0);
    }

    @Test
    public void test_number_input_double_hundred() throws Exception {
        // Assert.assertEquals(0.0010f, 0.0013f, 0.0003f);
        // Above is true because 0.0003 <= (0.0013 - 0.0010 = 0.0003)
        NumberFormat format = NFormat.currency();
        InputDouble input = new InputDouble(format, 0.0, Double.MAX_VALUE);
        input.append("4");
        Assert.assertEquals(4.0, input.getValueDouble(), 0.0);
        input.append("5");
        Assert.assertEquals(45.0, input.getValueDouble(), 0.0);
        input.append("6");
        Assert.assertEquals(456.0, input.getValueDouble(), 0.0);
        input.append("7");
        input.append(".");
        input.append("8");
        Assert.assertEquals(4567.80, input.getValueDouble(), 0.0);
        input.append("5");
        Assert.assertEquals(4567.85, input.getValueDouble(), 0.0);
    }

    @Test
    public void test_number_input_double_million() throws Exception {
        // Assert.assertEquals(0.0010f, 0.0013f, 0.0003f);
        // Above is true because 0.0003 <= (0.0013 - 0.0010 = 0.0003)

        NumberFormat format = NFormat.currency();
        InputDouble input = new InputDouble(format, 0.0, Double.MAX_VALUE);
        input.append("9");

        input.append("0");
        input.append("0");
        input.append("0");

        input.append("0");
        input.append("0");
        input.append("0");

        input.append("0");
        input.append("0");
        input.append("0");

        input.append("0");
        input.append("0");
        input.append("0");

        Assert.assertEquals(9000_000_000_000.0, input.getValueDouble(), 0.0);
        input.append(".");
        input.append("8");
        Assert.assertEquals(9000_000_000_000.80, input.getValueDouble(), 0.0);
        input.append("5");
        Assert.assertEquals(9000_000_000_000.85, input.getValueDouble(), 0.0);

        // Assert.assertEquals(4567.80, input.getValueDouble(), 0.0);
        // input.append("5");
        // Assert.assertEquals(4567.85, input.getValueDouble(), 0.0);
    }

}