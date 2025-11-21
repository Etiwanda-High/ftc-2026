package moe.seikimo.ftc.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import moe.seikimo.ftc.robot.fsm.State;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FiniteState {
    /** The state taken on. */
    State value();

    /** The state's override priority. */
    int priority() default 0;
}
