package com.grafsemmel.valium

import androidx.annotation.StringRes

class FieldValidatorBuilder {
    private val list: MutableList<FieldValidator> = mutableListOf()

    fun min(length: Int, @StringRes errorStringRes: Int = R.string.validation_error_too_short) = apply {
        list.add(BaseFieldValidator(Validators.MinLengthValidator(length), errorStringRes, length))
    }

    fun max(length: Int, @StringRes errorStringRes: Int = R.string.validation_error_too_long) = apply {
        list.add(BaseFieldValidator(Validators.MaxLengthValidator(length), errorStringRes, length))
    }

    fun length(length: Int, @StringRes errorStringRes: Int = R.string.validation_error_wrong_length) = apply {
        list.add(BaseFieldValidator(Validators.LengthValidator(length), errorStringRes, length))
    }

    fun notBlank(@StringRes errorStringRes: Int = R.string.validation_error_is_blank) = apply {
        list.add(BaseFieldValidator(Validators.NotBlankValidator(), errorStringRes))
    }

    fun range(
        from: Int,
        to: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_not_in_range
    ) = apply {
        list.add(BaseFieldValidator(Validators.RangeValidator(from, to), errorStringRes, from, to))
    }

    fun startsWith(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_starts_with
    ) = apply {
        list.add(
            BaseFieldValidator(
                Validators.StartsWithValidator(
                    *strings,
                    ignoreCase = ignoreCase,
                    mustNot = mustNot
                ),
                errorStringRes,
                strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
            )
        )
    }

    fun endsWith(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_ends_with
    ) = apply {
        list.add(
            BaseFieldValidator(
                Validators.EndsWithValidator(*strings, ignoreCase = ignoreCase, mustNot = mustNot),
                errorStringRes,
                strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
            )
        )
    }

    fun contains(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_contains
    ) = apply {
        list.add(
            BaseFieldValidator(
                Validators.ContainsValidator(*strings, ignoreCase = ignoreCase, mustNot = mustNot),
                errorStringRes,
                strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
            )
        )
    }

    fun regex(
        regex: Regex,
        @StringRes errorStringRes: Int = R.string.validation_error_regex,
        params: Any
    ) = apply {
        list.add(BaseFieldValidator(Validators.RegexValidator(regex), errorStringRes, params))
    }

    fun add(fieldValidator: FieldValidator) = apply { list.add(fieldValidator) }

    fun build(): FieldValidator = FieldValidatorChain(*list.toTypedArray())
}