package moe.seikimo.ftc.annotations.fields;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a field as being sourced from a prompt.
 * <p>
 * The declared field will automatically be initialized with the
 * value provided by the user in response to the prompt.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromPrompt {
    /**
     * @return The label of the prompt to source the value from.
     */
    String value();
}
