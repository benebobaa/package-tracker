package com.beneboba.package_tracking.util.constraint;

import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, UserRegisterRequest> {

    @Override
    public boolean isValid(UserRegisterRequest value, ConstraintValidatorContext constraintValidatorContext) {
        if (value.getPassword() == null || value.getConfirmPassword() == null) {
            return true;
        }

        return value.getPassword().equals(value.getConfirmPassword());
    }
}
