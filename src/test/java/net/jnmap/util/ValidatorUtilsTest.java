package net.jnmap.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucas.
 */
public class ValidatorUtilsTest {

    @Test
    public void testIsValid() {
        Map<String, Boolean> truthTable = new HashMap<>();
        truthTable.put("2001:0000:1234:0000:0000:C1C0:ABCD:0876 0", false);
        truthTable.put("2001:0db8:1234::", true);
        truthTable.put("2::10", true);
        truthTable.put("1::", true);
        truthTable.put("1:2:3::4:5::7:8", false);
        truthTable.put("123.23.34.2", true);
        truthTable.put("localhost", true);
        truthTable.put("google", false);
        truthTable.put("test.mydomain.com", true);
        truthTable.put("crowdstrike.com", true);

        for (Map.Entry<String, Boolean> check: truthTable.entrySet()) {
            Assert.assertTrue(check.getValue() == ValidatorUtils.isValidTarget(check.getKey()));
        }
    }
}
