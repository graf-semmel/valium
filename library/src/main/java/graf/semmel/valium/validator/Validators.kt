package graf.semmel.valium.validator

import android.telephony.PhoneNumberUtils
import androidx.core.util.PatternsCompat

object Validators {
    class NotBlankValidator : Validator {
        override fun validate(value: String) = value.isNotBlank()
    }

    class MaxLengthValidator(private val max: Int) : Validator {
        override fun validate(value: String) = isNotNegative(max) && value.length <= max
    }

    class MinLengthValidator(private val min: Int) : Validator {
        override fun validate(value: String) = isNotNegative(min) && value.length >= min
    }

    class LengthValidator(private val length: Int) : Validator {
        override fun validate(value: String) = isNotNegative(length) && value.length == length
    }

    class RangeValidator(private val from: Int, private val to: Int) : Validator {
        override fun validate(value: String) = isNotNegative(from)
                && isNotNegative(to)
                && isGreaterEquals(to, from)
                && value.length in from..to
    }

    class StartsWithValidator(
        private vararg val strings: String,
        private val ignoreCase: Boolean = true,
        private val mustNot: Boolean = false
    ) : Validator {
        override fun validate(value: String): Boolean {
            val startsWith = strings.find { value.startsWith(it, ignoreCase) } != null
            return if (mustNot) startsWith.not() else startsWith
        }
    }

    class EndsWithValidator(
        private vararg val strings: String,
        private val ignoreCase: Boolean = true,
        private val mustNot: Boolean = false
    ) : Validator {
        override fun validate(value: String): Boolean {
            val endsWith = strings.find { value.endsWith(it, ignoreCase) } != null
            return if (mustNot) endsWith.not() else endsWith
        }
    }

    class ContainsValidator(
        private vararg val strings: String,
        private val ignoreCase: Boolean = true,
        private val mustNot: Boolean = false
    ) : Validator {
        override fun validate(value: String): Boolean {
            val contains = strings.find { value.contains(it, ignoreCase) } != null
            return if (mustNot) contains.not() else contains
        }
    }

    private fun isNotNegative(number: Int): Boolean = when {
        number < 0 -> throw IllegalArgumentException("argument must not be negative but is $number")
        else -> true
    }

    private fun isGreaterEquals(numberA: Int, numberB: Int): Boolean = when {
        numberA < numberB -> throw IllegalArgumentException("first argument ($numberA) must be greater or equals second argument ($numberB)")
        else -> true
    }

    open class RegexValidator(private val regex: Regex) : Validator {
        override fun validate(value: String) = regex.toPattern().matcher(value).matches()
    }

    class EmailValidator : RegexValidator(PatternsCompat.EMAIL_ADDRESS.toRegex())

    class AlphaNumericValidator : Validators.RegexValidator("[ 0-9a-zA-Z/.\\-_‘,]+".toRegex())

    // TODO extend Rules class and write tests
    class PhoneNumberValidator : Validator {
        override fun validate(value: String) = PhoneNumberUtils.isGlobalPhoneNumber(value)
    }

}
