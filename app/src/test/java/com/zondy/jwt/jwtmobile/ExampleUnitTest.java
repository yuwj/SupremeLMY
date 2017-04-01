package com.zondy.jwt.jwtmobile;

import android.util.Log;

import com.zondy.jwt.jwtmobile.entity.EntityPredict;
import com.zondy.jwt.jwtmobile.util.GsonUtil;

import org.junit.Test;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGson(){
        String str = "{\"deptName\":\"浦楼 \",\"predResults\":[{\"realDate\":20170228,\"gridNum\":1144,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1144,\"p1X\":119.000694,\"p1Y\":33.579838,\"p2X\":119.00177,\"p2Y\":33.57894,\"centX\":119.00123,\"centY\":33.579388,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.1054},{\"realDate\":20170228,\"gridNum\":1375,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1375,\"p1X\":119.00932,\"p1Y\":33.580738,\"p2X\":119.01041,\"p2Y\":33.579838,\"centX\":119.009865,\"centY\":33.580288,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.093994856},{\"realDate\":20170228,\"gridNum\":1143,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1143,\"p1X\":119.000694,\"p1Y\":33.580738,\"p2X\":119.00177,\"p2Y\":33.579838,\"centX\":119.00123,\"centY\":33.580288,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.04595056},{\"realDate\":20170228,\"gridNum\":1173,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1173,\"p1X\":119.00177,\"p1Y\":33.579838,\"p2X\":119.00285,\"p2Y\":33.57894,\"centX\":119.00231,\"centY\":33.579388,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.034297008},{\"realDate\":20170228,\"gridNum\":1289,\"patrolPeroidId\":0,\"pdId\":91,\"intervalUpper\":\"0000\",\"intervalLower\":\"2400\",\"crimeGroupId\":null,\"gridInfo\":{\"pdId\":91,\"gridNum\":1289,\"p1X\":119.00609,\"p1Y\":33.579838,\"p2X\":119.00717,\"p2Y\":33.57894,\"centX\":119.00663,\"centY\":33.579388,\"subDistrictId\":0},\"duration\":null,\"checked\":false,\"predProb\":0.02798841}],\"date\":1488211200000}";
        EntityPredict predict =  GsonUtil.json2Bean(str, EntityPredict.class);
//        Log.i(TAG, "testGson: "+predict.toString());
    }
}