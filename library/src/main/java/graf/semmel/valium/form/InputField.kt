package graf.semmel.valium.form

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout
import com.grafsemmel.valium.R
import graf.semmel.valium.addTextChangedListener
import graf.semmel.valium.validator.ValidationResult
import graf.semmel.valium.validator.ValidationResult.NotValid
import graf.semmel.valium.validator.ValidationResult.Valid
import graf.semmel.valium.validator.isValid
import kotlin.properties.Delegates

class InputField(val id: Int) {

    private lateinit var editText: EditText
    private lateinit var errorView: ErrorView
    private var validateIf: (text: String) -> Boolean = { true }
    private val rules: Rules = Rules()
    private var validationResult: ValidationResult by Delegates.observable(NotValid(R.string.validation_error_generic)) { property, oldValue, newValue ->
        Log.d("DEBUG", "InputField isValid changed: $validationResult.isValid()")
        if (oldValue.isValid() != newValue.isValid()) onValidationChanged(isValid)
        updateError()
    }
    val isValid get() = validationResult.isValid()
    var onValidationChanged: (isValid: Boolean) -> Unit = {}
    var required: Boolean = true

    fun validateIf(predicate: (text: String) -> Boolean) {
        validateIf = predicate
    }

    fun validate() {
        val text = editText.text.toString()
        Log.d("DEBUG", "InputField validate $text")
        validationResult = if (!required && text.isBlank()) {
            Valid
        } else {
            rules.rules.map { it.validate(text) }.find { it is NotValid } ?: Valid
        }
    }

    private fun updateError() {
        validationResult.let { result ->
            errorView.showError(
                when (result) {
                    is NotValid -> editText.context.getString(result.errorStringRes, *result.placeholders)
                    is Valid -> null
                }
            )
        }
    }

    fun bindView(view: View) {
        when (view) {
            is EditText -> {
                this.editText = view
                this.errorView =
                    view.findTextInputLayoutParent()?.let { ErrorView.TextInputLayout(it) } ?: ErrorView.EditText(
                        editText
                    )
            }
            is TextInputLayout -> {
                this.editText = view.findFirstEditTextChild()
                    ?: throw IllegalArgumentException("TextInputLayouts must have at least one EditText as child.")
                this.errorView = ErrorView.TextInputLayout(view)
            }
            else -> throw IllegalArgumentException("Only EditText and TextInputLayout are allowed as view for an InputField.")
        }
        editText.apply { addTextChangedListener { validate() } }
        Log.d("DEBUG", "InputField resolveView")
    }

    fun rules(config: Rules.() -> Unit) = rules.apply(config)

    sealed class ErrorView {

        abstract fun showError(error: String?)

        class EditText(val view: android.widget.EditText) : ErrorView() {

            override fun showError(error: String?) {
                view.error = error
            }
        }

        class TextInputLayout(val view: com.google.android.material.textfield.TextInputLayout) : ErrorView() {

            override fun showError(error: String?) {
                view.error = error
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

