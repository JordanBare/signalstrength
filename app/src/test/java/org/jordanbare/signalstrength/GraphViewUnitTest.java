package org.jordanbare.signalstrength;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jordanbare on 1/23/18.
 */
@RunWith(RobolectricTestRunner.class)
public class GraphViewUnitTest {

    @Test
    public void calculateTopYMathIsAccurate(){
        Context context = ShadowApplication.getInstance().getApplicationContext();
        int height = 700;
        int width = 300;
        GraphView graphView = new GraphView(context, width, height);

        ArrayList<Map> cellInfoMapList = new ArrayList<>();
        Map<Object, Serializable> map = new HashMap<>();

        int[] testValues = {-130, -110, -100, -90, -70, -50};

        for(int i = 0; i < testValues.length; i++){
            cellInfoMapList.add(map);
        }

        graphView.initializeDataToDraw(cellInfoMapList);

        graphView.setdBmValues(testValues);

        float[] actualTopYValues = graphView.calculateTopY();
        float[] expectedTopYValues = {700f, 525f, 437.5f, 350f, 175f, 0f};

        Assert.assertArrayEquals(expectedTopYValues, actualTopYValues, 1.0f);
    }

    @Test
    public void test(){
        int height = 700;
        int oldMin = -130;
        int oldRange = 80;

        float actual = height - (((-110 - oldMin) * height) / oldRange);
        float expected = 525f;
        Assert.assertEquals(expected, actual,0.0);
    }

}
