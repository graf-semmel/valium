![master branch batch](https://github.com/graf-semmel/valium/actions/workflows/master.yml/badge.svg)

# Valium
Android library to validate forms

## Download

TODO

## Example

```kotlin
// in your activity or fragment
val form = form {
    inputField(R.id.et_first_name) {
        rules {
            notBlank()
            min(3)
            max(100)
        }
    }
    inputField(R.id.et_last_name) {
        rules {
            notBlank()
            min(3)
            max(100)
        }
    }
    inputField(R.id.et_email) {
        rules {
            notBlank()
            isEmail()
        }
    }
    submitButton(R.id.btn_submit)
    onValidationChange { /* isValid -> viewModel.onFormValidationChange(isValid) */ }
}
```
<img width="500" alt="portfolio_view" src="form-validation.gif">

### Mark fields as optional

```kotlin
inputField {
    required = false
}
```

### Custom error message

```kotlin
rules {
    notBlank(R.string.my_not_blank)
    min(3, R.string.my_too_short)
}
```

### Extend validation rules

```kotlin
// extend Rule interface or ValidatorRule class if you want to use Validators
class EmailRule(@StringRes errorStringRes: Int = R.string.validation_error_email) : ValidatorRule(
    Validators.EmailValidator(),
    errorStringRes
)

// extend Rules class
fun Rules.isEmail(@StringRes errorStringRes: Int = R.string.validation_error_email) = this.addRule(EmailRule())

// add to rules
inputField(R.id.et_email) {
    rules {
        isEmail()
    }
}
```

### Custom validators

Validators will be implemented by extending the `Validator` interface
```kotlin
interface Validator {
    fun validate(value: String): Boolean
}
```
### Default validators
Common validators are provided under the `Validators` class
```kotlin
object Validators {
    class NotBlankValidator : Validator {
        override fun validate(value: String) = value.isNotBlank()
    }

    class MaxLengthValidator(private val max: Int) : Validator {
        override fun validate(value: String) = isNotNegative(max) && value.length <= max
    }
		...
}
```

## TODO
* publish package
* spinner field
* checkbox
* more rules
* validation under condition
