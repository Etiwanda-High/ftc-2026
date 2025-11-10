package moe.seikimo.ftc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a class as an individual system.
 * <p>
 * The declared class will be injected into classes.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RobotSystem {
    /**
     * @return When the system should be loaded.
     */
    Priority value() default Priority.NORMAL;

    /**
     * The priority of system loading.
     */
    enum Priority {
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST
    }
}
