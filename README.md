# Valium
Validation library for Android

A Validator will be implemented by extending the interface Validator

### Validator
```kotlin
interface Validator {
    fun validate(value: String): Boolean
}
```
### Validators
Common validators are provided under the Validators class
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
## Input Validation
Validation in general checks if a given input matches the requirements. The output will be a simple boolean. In Android we want to couple this validation rules to input components. Further we might want to show that the input is correct or a message if not. To achieve this we use FieldValidator. A FieldValidator validates a given input string as well but it returns a ValidationState.

### FieldValidator
```kotlin
interface FieldValidator {
    fun validate(value: String): ValidationState
    fun getErrorState(): ValidationState
}
```
### ValidationState
A ValidationState is of type 
- Valid
- NotValid
  - This state also provides a string resource which is the message we display the user if the input is not valid. You can attach additional placeholder values if your string resource contains placeholders.
```kotlin
ValidationState.isValid() // returns true if the state is of type Valid, otherwise false
```
Now when a FieldValidator validates input and the input is not valid we use the message from the ValidationState and show it to the user.

## Using validators and error messages
To use Validators for validation user input and showing error messages to the user we extend from FieldValidator. This is already done with BaseFieldValidator which binds both concepts loosely together. A BaseFieldValidator takes a Validator which is responsible for validating the input and an error message string plus placeholders in case the input is not valid.

### BaseFieldValidator
```kotlin
open class BaseFieldValidator(
    protected val validator: Validator,
    @StringRes protected open val errorStringRes: Int,
    private vararg val placeholders: Any?
) : FieldValidator {
    override fun validate(value: String): ValidationState = when (validator.validate(value)) {
        true -> ValidationState.Valid
        else -> getErrorState()
    }

    override fun getErrorState() =
        ValidationState.NotValid(errorStringRes, *placeholders)
}
```
Which this approach you easily pair validators and error messages
```kotlin
val emailValidator = BaseFieldValidator(Validators.EmailValidator(), R.string.email_not_valid)
val addressValidator = BaseFieldValidator(Validators.NotBlankValidator(), R.string.address_error_message)
```
### FieldValidatorChain
If you want to concatenate validators together than you can use a FieldValidatorChain. A FieldValidatorChain validated the input according the order of its validators and exits the validation with the error message of the first failing validator.
```kotlin
val cardActivationCodeValidator = FieldValidatorChain(
    BaseFieldValidator(Validators.NotBlankValidator(), R.string.activation_field_error), // first validation
    BaseFieldValidator(Validators.MinLengthValidator(4), R.string.activation_invalid_code) // second validation
)
```
### FieldValidatorBuilder
To simplify the chaining and creation of field validator you can use the FieldValidatorBuilder class that provides an easy interface to build up validators and chain them together.
```kotlin
val validator = FieldValidatorBuilder()
    .notBlank(R.string.warning_please_enter_three_digits)
    .length(3, R.string.warning_please_enter_three_digits)
    .regex("[0-9]+".toRegex(), R.string.warning_please_enter_three_digits)
    .create()
```
## Binding validation to input fields
How to bind validation to user input now? We want to have a solution that validates every time the input is changing. The simplest solution is using the a text change listener on the EditText. To avoid boiler code you can use the bindTo() method.

### bindTo()
```kotlin
val editText: EditText
val validator: FieldValidator
editText.addTextChangedListener(afterTextChanged = { text: Editable? ->
    editText.error = when (val state = validator.validate(text.toString())) {
        ValidationState.Valid -> null
        is ValidationState.NotValid -> context.getString(
            state.errorStringRes,
            *state.placeholders
        )
    }
})
// or shorter
validator.bindTo(editText)
```
### LiveData
Luckelly Android provides LiveData to make data observable and enables reactive interaction patterns. To bind a validation result to an input component we use the Field class. A Field class is an abstraction of an input field which holds an validation state.

#### Field
```kotlin
open class Field(var isOptional: Boolean = false) {
    val validationState: MediatorLiveData<ValidationState> = MediatorLiveData()
}
```
The most common input component is the EditText class. The Field abstraction of en EditText object is the InputField. The InputField provides additionally to the validation state also text and error LiveData. The InputField will validates the value of the text LiveData whenever its get changes. That means a view that contains an EditText has to populate its change to the text LiveData of the InputField.

#### InputField
```kotlin
class InputField(validator: FieldValidator? = null, isOptional: Boolean = false) : Field(isOptional) {
    val text: MutableLiveData<String> = MutableLiveData()
    val error = MediatorLiveData<ValidationState>()
	...
}
// example in view model
val addressField = InputField(FieldValidators.addressValidator)
```
Usage
```kotlin
// update address InputField text in viewmodel
val editText: EditText
editText.addTextChangedListener(afterTextChanged = { text: Editable? ->
    viewModel.addressField.text.value = text.toString()
})
// receive validation errors from address Inputfield
viewModel.observe(viewmodel.addressField.error, ::onValidationError)
fun onValidationError(state: ValidationState?) {
    (state as? NotValid)?.let { editText.error = context.getString(it.errorStringRes, *it.placeholders) }
}
```
#### additional parameter
- isOptional:Boolean - the field is valid even there is not user input, but if there is one the validation is triggered
- showErrorOnInputChange:Boolean - the validation happens every time the input (text field) is changing
- showErrorOnInputChangeThreshold:Int - the validation happens it the input length is longer than the threshold
### DataBinding
Databinding removes boiler code from your views.
```kotlin
// view model
class AddressViewModel() : ViewModel() {
    val addressField = InputField(FieldValidators.addressValidator)
}
```
```xml
// xml with view model as data binding variable 
<EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@={viewmodel.addressField.text}"
    app:error="@{viewmodel.addressField.error}" />
```
## Form
Often you want to now the the validation result of multiple input events in your screen. Imagine you have a form for the address details of an user and you have a submit button that should be only enable if all fields are valid. To collect the validation result of multiple field you can use the Form class. Simply add you validators to the form by calling addField()
```kotlin
class SampleForm : Form() {
    val nameValidator = InputField(FieldValidatorBuilder().min(3).max(32).build()).also { addField(it) }
    val emailValidator = InputField(BaseFieldValidator(Validators.EmailValidator(), R.string.validation_error_email)).also { addField(it) }
}
```
You can then bind the form validation result to a button for example.
```kotlin
// view model
class FormViewModel : ViewModel() {
    val form = SampleForm()
}
// receive validation result from your form
viewModel.observe(viewmodel.form.validation, ::onFormValidation)
fun onFormValidation(isValid: boolean?) {
    button.enabled = it ?: false
}
```
### Databinding
Or you can use data binding
```xml
// xml with viewmodel as data binding variable 
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:enabled="@{viewmodel.form.validation}" />
```
## TODO
* publish to maven
* optional parameter docs
* SpinnerField docs
* prevent validation on typing docs
* write Medium article
* get famous
