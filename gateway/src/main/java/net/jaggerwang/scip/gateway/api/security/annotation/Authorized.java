package net.jaggerwang.scip.gateway.api.security.annotation;

import org.springframework.core.annotation.AliasFor;

import javax.annotation.security.RolesAllowed;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorized {
    @AliasFor(annotation = RolesAllowed.class)
    String[] value();
}
