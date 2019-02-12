package org.github.clbuttic.sodium4j.annotation;

import java.lang.annotation.*;

/**
 * An annotation used to decorate SodiumLibrary and indicate when a function was introducted into libSodium.
 *
 * This helps backwards compatibility, by preventing calls to non-existent functions.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Since {
    public String value();
}
