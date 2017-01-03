package hcfs.test.utils;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtilsTest {
    @Test
    public void testToMap() throws Exception{
        JSONObject obj = new JSONObject("{result:true, code:1, data:{a:1234, b:'abc', c:false}}");
        Map<String, Object> cases = JsonUtils.toMap(obj);
        assertTrue((boolean) cases.get("result"));
        assertEquals(1, cases.get("code"));
        assertEquals(1234,  ((Map)cases.get("data")).get("a"));
        assertEquals("abc", ((Map)cases.get("data")).get("b"));
        assertFalse((boolean) ((Map)cases.get("data")).get("c"));
    }

    @Test
    public void testIgnore() throws Exception{
        JSONObject obj = new JSONObject("{result:true, code:1, data:{a:1234, b:'abc', c:false}}");
        Map<String, Object> cases = JsonUtils.toMap(obj);
        assertTrue((boolean) cases.get("result"));
        assertEquals(1, cases.get("code"));
        assertEquals(1234,  ((Map)cases.get("data")).get("a"));
        assertEquals("abc", ((Map)cases.get("data")).get("b"));
        assertFalse((boolean) ((Map)cases.get("data")).get("c"));
    }


    @Test
    public void testRandom() throws Exception{
        String test1 = "09-07 16:57:36.542  4357  9617 D HopeBay : MgmtCluster(getDeviceServiceInfo): log content";
        Pattern timePattern = Pattern.compile("\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}");
        Pattern pattern = Pattern.compile(" +");
        Matcher matcher = timePattern.matcher(test1);
        assertTrue(matcher.find());
        assertEquals("09-07 16:57:36.542", matcher.group(0));
        String[] arr = pattern.split(test1, 8);
        assertEquals("09-07", arr[0]);
        assertEquals("16:57:36.542", arr[1]);
        assertEquals("4357", arr[2]);
        assertEquals("9617", arr[3]);
        assertEquals("D", arr[4]);
        assertEquals("HopeBay", arr[5]);
        assertEquals(":", arr[6]);
        assertEquals("MgmtCluster(getDeviceServiceInfo): log content", arr[7]);
    }
}