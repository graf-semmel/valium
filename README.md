# Valium
Android ibrary to validate forms.

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

### Mark Fields As Optional

```kotlin
rules {
    required = false
}
```

### Custom Error Message

```kotlin
rules {
		notBlank(R.string.my_not_blank)
  	min(3, R.string.my_too_short)
}
```

### Extend Validation Rules

```kotlin
// extends interface Rule or class ValidatorRule if you want to use Validators
class EmailRule(@StringRes errorStringRes: Int = R.string.validation_error_email) : ValidatorRule(
    Validators.EmailValidator(),
    errorStringRes
)

// extends Rules class
fun Rules.isEmail(@StringRes errorStringRes: Int = R.string.validation_error_email) = this.addRule(EmailRule())

// use in input field definition
inputField(R.id.et_email) {
    rules {
        isEmail()
    }
}
```

### Custom Validators

Validators will be implemented by extending the `Validator` interface
```kotlin
interface Validator {
    fun validate(value: String): Boolean
}
```
### Default Validators
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
* publish to maven
* optional parameter
* SpinnerField docs
* prevent validation on typing docs
