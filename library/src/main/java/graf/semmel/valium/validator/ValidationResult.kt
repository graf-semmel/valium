package graf.semmel.valium.validator

import androidx.annotation.StringRes

sealed class ValidationResult {
    object Valid : ValidationResult()
    class NotValid(@StringRes val errorStringRes: Int = 0, vararg val placeholders: Any) : ValidationResult()

    companion object {
        fun from(isValid: Boolean): ValidationResult = when {
            isValid -> Valid
            else -> NotValid()
        }
    }
}

fun ValidationResult.isValid(): Boolean = this is ValidationResult.Valid