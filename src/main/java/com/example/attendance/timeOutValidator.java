package com.example.attendance;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class timeOutValidator implements ConstraintValidator<timeCheck, Employee> {

    @Override
    public void initialize(timeCheck timeOut) {
    }

    @Override
    public boolean isValid(Employee theEmployee, ConstraintValidatorContext cxt) {
        boolean valid = true;

        if (theEmployee.getTimeOut() != null) {
            int value = theEmployee.getTimeIn().compareTo(theEmployee.getTimeOut());
            if (value >= 0) {
                valid = false;
            }
        }
        return valid;
    }


}