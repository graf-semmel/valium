package graf.semmel.valium.form

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.grafsemmel.valium.R
import graf.semmel.valium.validator.ValidationResult
import graf.semmel.valium.validator.ValidationResult.NotValid
import graf.semmel.valium.validator.ValidationResult.Valid
import graf.semmel.valium.validator.isValid
import kotlin.properties.Delegates

class InputField(val id: Int) {
    private var firstFocus = true
    private lateinit var editText: EditText
    private lateinit var errorView: ErrorView
    private val rules: Rules = Rules()
    private var showErrors: ShowErrors? = null
    private var validationResult: ValidationResult by Delegates.observable(NotValid(R.string.validation_error_generic)) { _, oldValue, newValue ->
        Log.d("DEBUG", "InputField isValid changed: $validationResult")
        if (oldValue.isValid() != newValue.isValid()) onValidationChanged(isValid)
    }

    val text: String get() = editText.text.toString()

    val isValid: Boolean get() = validationResult.isValid()

    var onValidationChanged: (isValid: Boolean) -> Unit = {}

    var required: Boolean = true

    fun validate() {
        Log.d("DEBUG", "InputField validate $text")
        validationResult = if (!required && text.isBlank()) Valid else rules.validate(text)
    }

    private fun updateError() {
        val hasLostFocus = !editText.hasFocus()
        if (firstFocus && hasLostFocus) firstFocus = false
        showError(showErrors?.shouldShow(hasLostFocus, firstFocus) ?: false)
    }

    fun showError(visible: Boolean = false) = with(validationResult) {
        when (this) {
            is Valid -> errorView.hideError()
            is NotValid -> if (visible) {
                val error = editText.context.getString(errorStringRes, *placeholders)
                errorView.showError(error)
            } else {
                errorView.hideError()
            }
        }
    }

    fun setupView(view: View, showErrors: ShowErrors) {
        Log.d("DEBUG", "InputField bindView")
        this.showErrors = this.showErrors ?: showErrors
        when (view) {
            is EditText -> {
                this.editText = view
                this.errorView = view.findTextInputLayoutParent()
                    ?.let { ErrorView.TextInputLayout(it) }
                    ?: ErrorView.EditText(editText)
            }
            is TextInputLayout -> {
                this.editText = view.findFirstEditTextChild()
                    ?: throw IllegalArgumentException("TextInputLayouts must have at least one EditText as child.")
                this.errorView = ErrorView.TextInputLayout(view)
            }
            else -> throw IllegalArgumentException("Only EditText and TextInputLayout are allowed as view for an InputField.")
        }
        editText.doAfterTextChanged {
            validate()
            updateError()
        }
        editText.setOnFocusChangeListener { _, _ ->
            validate()
            updateError()
        }
    }

    fun rules(config: Rules.() -> Unit) = rules.apply(config)

    fun showErrors(config: ShowErrors.() -> Unit) {
        this.showErrors = ShowErrors().apply(config)
    }

    sealed class ErrorView {

        abstract fun showError(error: String)
        abstract fun hideError()

        class EditText(private val view: android.widget.EditText) : ErrorView() {

            override fun showError(error: String) {
                view.error = error
            }

            override fun hideError() {
                view.error = null
            }
        }

        class TextInputLayout(private val view: com.google.android.material.textfield.TextInputLayout) : ErrorView() {

            override fun showError(error: String) {
                view.error = error
            }

            override fun hideError() {
                view.error = null
            }
        }
    }
}

private fun TextInputLayout.findFirstEditTextChild(view: View = this): EditText? {
    if (view is ViewGroup) {
        view.children.forEach { child -> findFirstEditTextChild(child)?.let { return it } }
    }
    return view as? EditText
}

private fun View.findTextInputLayoutParent(view: View? = this): TextInputLayout? {
    if (view == null) return null
    val parent = view.parent
    if (parent is TextInputLayout) return parent
    return findTextInputLayoutParent(parent as? View)
}

