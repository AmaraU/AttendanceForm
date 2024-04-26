package com.example.attendance;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = timeOutValidator.class)
@Target( {ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface timeCheck {
    String message() default "must be after Time In";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


//@Constraint(validatedBy = CourseCodeConstraintValidator.class)
//@Target({ElementType.METHOD, ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface CourseCode {
//
//    public String value() default "LUV";
//
//    public String message() default "must start with LUV";
//
//    public Class<?>[] groups() default {};
//
//    public Class<? extends Payload>[] payload() default {};
//}



//    @Constraint(validatedBy=timeOutValidator.class)
//    @Target({ElementType.FIELD, ElementType.METHOD})
//    @Retention(RetentionPolicy.RUNTIME)
//
//    public @interface timeCheck {
//        String message() default "must be after {timeIn}";
//        Class<?>[] groups() default {};
//        Class<? extends Payload>[] payload() default {};
//        String value();
//    }
