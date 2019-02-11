package org.github.clbuttic.sodium4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to decorate SodiumLibrary and indicate when a function was introducted into libSodium.
 *
 * This helps backwards compatibility, by preventing calls to non-existent functions.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Since {
    public String value();
}
