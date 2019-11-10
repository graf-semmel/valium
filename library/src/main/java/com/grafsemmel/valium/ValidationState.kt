package com.grafsemmel.valium

import androidx.annotation.StringRes

sealed class ValidationState {
    object Valid : ValidationState()
    class NotValid(@StringRes val errorStringRes: Int = 0, vararg val placeholders: Any) : ValidationState()
    companion object {
        fun from(isValid: Boolean): ValidationState = when {
            isValid -> Valid
            else -> NotValid()
        }
    }
}

fun ValidationState.isValid(): Boolean = this is ValidationState.Valid