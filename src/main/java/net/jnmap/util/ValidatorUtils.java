package net.jnmap.util;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Created by lucas on 8/31/15.
 */
public class ValidatorUtils {
    public static boolean isValidTarget(String target) {
        InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
        DomainValidator domainValidator = DomainValidator.getInstance();

        return inetAddressValidator.isValid(target) || domainValidator.isValid(target) || domainValidator.isValidLocalTld(target);
    }
}
