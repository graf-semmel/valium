package graf.semmel.valium.form

import androidx.annotation.StringRes
import com.grafsemmel.valium.R
import graf.semmel.valium.validator.ValidationResult
import graf.semmel.valium.validator.ValidationResult.NotValid
import graf.semmel.valium.validator.ValidationResult.Valid
import graf.semmel.valium.validator.Validator
import graf.semmel.valium.validator.Validators

class Rules {

    private val rules = mutableSetOf<Rule>()

    fun min(
        length: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_too_short
    ) = registerRule(
        Validators.MinLengthValidator(length).asValidatorRule(
            errorStringRes,
            length
        )
    )

    fun max(
        length: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_too_long
    ) = registerRule(
        Validators.MaxLengthValidator(length).asValidatorRule(
            errorStringRes,
            length
        )
    )

    fun length(
        length: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_wrong_length
    ) = registerRule(
        Validators.LengthValidator(length).asValidatorRule(
            errorStringRes,
            length
        )
    )

    fun range(
        from: Int, to: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_not_in_range
    ) {
        Validators.RangeValidator(from, to).asValidatorRule(
            errorStringRes,
            from,
            to
        )
    }

    fun notBlank(
        @StringRes errorStringRes: Int = R.string.validation_error_is_blank
    ) = registerRule(Validators.NotBlankValidator().asValidatorRule(errorStringRes))

    fun startsWith(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_starts_with
    ) = registerRule(
        Validators.StartsWithValidator(
            *strings,
            ignoreCase = ignoreCase,
            mustNot = mustNot
        ).asValidatorRule(
            errorStringRes,
            strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
        )
    )

    fun endsWith(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_ends_with
    ) = registerRule(
        Validators.EndsWithValidator(*strings, ignoreCase = ignoreCase, mustNot = mustNot).asValidatorRule(
            errorStringRes,
            strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
        )
    )

    fun contains(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_contains
    ) = registerRule(
        Validators.ContainsValidator(*strings, ignoreCase = ignoreCase, mustNot = mustNot).asValidatorRule(
            errorStringRes,
            strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
        )
    )

    fun isEmail(
        @StringRes errorStringRes: Int = R.string.validation_error_email
    ) = registerRule(Validators.EmailValidator().asValidatorRule(errorStringRes))

    fun regex(
        regex: Regex,
        @StringRes errorStringRes: Int = R.string.validation_error_regex,
        params: Any
    ) = registerRule(Validators.RegexValidator(regex).asValidatorRule(errorStringRes, params))

    fun registerRule(rule: Rule) = rules.add(rule)

    fun validate(text: String): ValidationResult = rules.map { it.validate(text) }.find { it is NotValid } ?: Valid
}

open class ValidatorRule(
    private val validator: Validator,
    private val errorStringRes: Int,
    private vararg val errorStringArgs: Any
) : Rule {

    override fun validate(text: String) = when (validator.validate(text)) {
        true -> Valid
        false -> NotValid(errorStringRes, *errorStringArgs)
    }
}

fun Validator.asValidatorRule(
    errorStringRes: Int,
    vararg errorStringArgs: Any
): ValidatorRule = ValidatorRule(this, errorStringRes, *errorStringArgs)

interface Rule {

    fun validate(text: String): ValidationResult
}