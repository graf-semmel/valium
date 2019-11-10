package com.grafsemmel.valium

import androidx.annotation.StringRes

open class BaseFieldValidator(
    protected val validator: Validator,
    @StringRes protected open val errorStringRes: Int,
    private vararg val placeholders: Any
) : FieldValidator {
    override fun validate(value: String): ValidationState = when (validator.validate(value)) {
        true -> ValidationState.Valid
        else -> getErrorState()
    }

    override fun getErrorState() = ValidationState.NotValid(errorStringRes, *placeholders)
}