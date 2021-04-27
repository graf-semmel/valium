![master branch batch](https://github.com/graf-semmel/valium/actions/workflows/master.yml/badge.svg)
[![](https://jitpack.io/v/graf-semmel/valium.svg)](https://jitpack.io/#graf-semmel/valium)

# Valium
Android library to validate forms

## Download

```groovy
// build.gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

// app/build.gradle
dependencies {
    ...
    implementation 'com.github.graf-semmel:valium:0.4'
}
```

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

// extend Rules class and register new rule
fun Rules.isEmail(@StringRes errorStringRes: Int = R.string.validation_error_email) = registerRule(EmailRule())

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

### Change error displaying strategy

If you want to change the events that triggers the displaying of errors, overwrite the settings of the `showErrors` attribute. By default the errors will be shown when the user leaves the field he typed into. While typing their will be no error displayed when it is the first edit for a field.

If the form is linked to a submit button, any click on the submit button will trigger form validation and causes the displaying of potential errors. The possible settings are shown in the example below.

```kotlin
form {
    inputField(R.id.et_1_show_errors) {
        rules {
            notBlank()
            min(3)
        }
        showErrors { // will overwrite global validation strategy for a single field
            onFirstEdit = false
            onSubsequentEdit = true
            onLeaveField = true
        }
    }
    showErrors { // global validation strategy for a form
        onFirstEdit = false // when user types into field the very first time
        onSubsequentEdit = true // when user left the field and comes back to change its input
        onLeaveField = true // when user clicks into the next field
    }
}
```

## TODO
* spinner field
* checkbox
* more rules
* move package to Maven Central