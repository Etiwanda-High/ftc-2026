package moe.seikimo.ftc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a field as a hardware map reference.
 * <p>
 * The declared field will automatically be initialized with the
 * corresponding hardware device from the hardware map.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hardware {
    /**
     * @return The hardware map name of the device.
     */
    String value();
}
