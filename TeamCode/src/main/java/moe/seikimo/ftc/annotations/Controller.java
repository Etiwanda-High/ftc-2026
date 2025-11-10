package moe.seikimo.ftc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    /** @return The player associated with this field. */
    Player value();

    /**
     * The player for this controller.
     */
    enum Player {
        DRIVER,
        OPERATOR
    }
}
