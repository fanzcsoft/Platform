/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.alliander.osgp.domain.core.valueobjects.LightValue;

public class LightValueConstraintsValidator implements ConstraintValidator<LightValueConstraints, LightValue> {

    private static final String CHECK_DIM_VALUE_EMPTY_WHEN_LIGHT_OFF = "Dim value may not be set when light is switched off";

    // Index should be between 0 and 6 --> using annotations
    // Dim Value should be between 1 and 100 --> using annotations
    // If On = false, Dim Value may not be set

    @Override
    public void initialize(final LightValueConstraints constraintAnnotation) {
        // Empty Method
    }

    @Override
    public boolean isValid(final LightValue lightValue, final ConstraintValidatorContext context) {
        final ValidatorHelper helper = new ValidatorHelper();

        this.checkDimValue(helper, lightValue);

        return helper.isValid(context);
    }

    private void checkDimValue(final ValidatorHelper helper, final LightValue lightValue) {
        if (!lightValue.isOn() && lightValue.getDimValue() != null) {
            helper.addMessage(CHECK_DIM_VALUE_EMPTY_WHEN_LIGHT_OFF);
        }
    }
}
