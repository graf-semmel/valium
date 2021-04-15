package graf.semmel.valium.form

import androidx.annotation.StringRes
import com.grafsemmel.valium.R
import graf.semmel.valium.validator.ValidationResult
import graf.semmel.valium.validator.ValidationResult.NotValid
import graf.semmel.valium.validator.ValidationResult.Valid
import graf.semmel.valium.validator.Validator
import graf.semmel.valium.validator.Validators

class Rules {

    val rules = mutableSetOf<Rule>()

    fun min(
        length: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_too_short
    ) = addRule(
        Validators.MinLengthValidator(length).asValidatorRule(
            errorStringRes,
            length
        )
    )

    fun max(
        length: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_too_long
    ) = addRule(
        Validators.MaxLengthValidator(length).asValidatorRule(
            errorStringRes,
            length
        )
    )

    fun length(
        length: Int,
        @StringRes errorStringRes: Int = R.string.validation_error_wrong_length
    ) = addRule(
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
    ) = addRule(Validators.NotBlankValidator().asValidatorRule(errorStringRes))

    fun startsWith(
        vararg strings: String,
        ignoreCase: Boolean = true,
        mustNot: Boolean = false,
        @StringRes errorStringRes: Int = R.string.validation_error_starts_with
    ) = addRule(
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
    ) = addRule(
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
    ) = addRule(
        Validators.ContainsValidator(*strings, ignoreCase = ignoreCase, mustNot = mustNot).asValidatorRule(
            errorStringRes,
            strings.joinToString(separator = "", prefix = "\"", postfix = "\"")
        )
    )

    fun isEmail(
        @StringRes errorStringRes: Int = R.string.validation_error_email
    ) = addRule(Validators.EmailValidator().asValidatorRule(errorStringRes))

    fun regex(
        regex: Regex,
        @StringRes errorStringRes: Int = R.string.validation_error_regex,
        params: Any
    ) = addRule(Validators.RegexValidator(regex).asValidatorRule(errorStringRes, params))

    fun addRule(rule: Rule) = rules.add(rule)
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

class EmailRule(@StringRes errorStringRes: Int = R.string.validation_error_email) : ValidatorRule(
    Validators.EmailValidator(),
    errorStringRes
)


fun Validator.asValidatorRule(
    errorStringRes: Int,
    vararg errorStringArgs: Any
): ValidatorRule = ValidatorRule(this, errorStringRes, *errorStringArgs)

interface Rule {

    fun validate(text: String): ValidationResult
}