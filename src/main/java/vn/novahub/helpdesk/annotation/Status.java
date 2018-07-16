package vn.novahub.helpdesk.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StatusConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {

    public String message() default "";

    public Class<?>[] groups() default {};

    public Class<?> targetClass() default Object.class;

    public Class<? extends Payload>[] payload() default {};
}
