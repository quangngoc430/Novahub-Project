package vn.novahub.helpdesk.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AccountStatusConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountStatus {

    public String message() default "";

    public Class<?>[] groups() default {};

    public String[] statuses();

    public Class<? extends Payload>[] payload() default {};
}
