package com.grafsemmel.valium

class FieldValidatorChain(vararg val validators: FieldValidator) : FieldValidator {
    private var validationState: ValidationState = ValidationState.Valid

    override fun validate(value: String): ValidationState {
        val notValidValidator = validators.find { it.validate(value) is ValidationState.NotValid }
        validationState = when (notValidValidator) {
            null -> ValidationState.Valid
            else -> notValidValidator.getErrorState()
        }
        return validationState
    }

    override fun getErrorState(): ValidationState = validationState
}