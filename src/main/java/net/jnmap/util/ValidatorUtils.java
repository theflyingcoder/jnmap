package net.jnmap.util;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Created by lucas.
 */
public class ValidatorUtils {

    /**
     * Checks if target satisfies the internet protocol and domain formats.
     *
     * @param target
     * @return
     */
    public static boolean isValidTarget(String target) {
        InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
        DomainValidator domainValidator = DomainValidator.getInstance();

        return inetAddressValidator.isValid(target) || domainValidator.isValid(target) || domainValidator.isValidLocalTld(target);
    }
}
