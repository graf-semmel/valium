package graf.semmel.valium.form

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlin.properties.Delegates

class Form {

    private var submitButton: SubmitButton? = null
    private val inputFields = mutableListOf<InputField>()
    private var onValidationChanged: (isValid: Boolean) -> Unit = {}
    private val showErrors: ShowErrors = ShowErrors()

    var isValid by Delegates.observable(true) { _, oldValue, newValue ->
        Log.d("DEBUG", "Form isValid changed: $newValue")
        submitButton?.enable(newValue)
        if (oldValue != newValue) onValidationChanged(newValue)
    }
        private set

    fun onValidationChange(listener: (isValid: Boolean) -> Unit = {}) {
        onValidationChanged = listener
    }

    fun validate() {
        Log.d("DEBUG", "Form validateAllInputFields")
        isValid = inputFields.onEach { it.validate() }.all { it.isValid }
    }

    private fun showErrors() {
        Log.d("DEBUG", "Form showErrors")
        inputFields.onEach { it.showError(true) }
    }

    fun inputField(id: Int, config: InputField.() -> Unit) {
        Log.d("DEBUG", "Form withInputField")
        InputField(id)
            .apply(config)
            .let { field ->
                field.onValidationChanged = { isValid = inputFields.all { it.isValid } }
                inputFields.add(field)
            }
    }

    fun submitButton(id: Int, config: SubmitButton.() -> Unit = {}) {
        Log.d("DEBUG", "Form withSubmitButton")
        submitButton = SubmitButton(id).apply { config() }
    }

    fun showErrors(config: ShowErrors.() -> Unit) = showErrors.apply(config)

    fun setupViews(rootView: View) {
        Log.d("DEBUG", "Form initViews")
        setupInputFields(rootView)
        setupSubmitButton(rootView)
    }

    private fun setupSubmitButton(rootView: View) {
        submitButton?.let { button ->
            val view = rootView.findViewById<View>(button.id)
            view.setOnClickListener {
                validate()
                showErrors()
            }
            button.setupView(view)
        }
    }

    private fun setupInputFields(rootView: View) {
        inputFields.onEach {
            it.setupView(rootView.findViewById(it.id), showErrors)
        }
    }

}

fun Fragment.form(config: Form.() -> Unit): Form {
    val form = Form().apply(config)
    viewLifecycleOwnerLiveData.observe(this) { lifecycleOwner ->
        lifecycleOwner.lifecycleScope.launchWhenCreated {
            form.apply {
                setupViews(requireView())
                validate()
            }
        }
    }
    return form
}
