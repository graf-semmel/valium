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
    private var isValid by Delegates.observable(true) { _, oldValue, newValue ->
        Log.d("DEBUG", "Form isValid changed: $newValue")
        submitButton?.enable(newValue)
        if (oldValue != newValue) onValidationChanged(newValue)
    }

    fun onValidationChange(listener: (isValid: Boolean) -> Unit = {}) {
        onValidationChanged = listener
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

    fun submitButton(id: Int) {
        Log.d("DEBUG", "Form withSubmitButton")
        submitButton = SubmitButton(id)
    }

    fun validate() {
        Log.d("DEBUG", "Form validateAllInputFields")
        isValid = inputFields.onEach { it.validate() }.all { it.isValid }
    }

    fun initViews(rootView: View) {
        Log.d("DEBUG", "Form initViews")
        inputFields.onEach { it.bindView(rootView.findViewById(it.id)) }
        submitButton?.apply { bindView(rootView.findViewById(id)) }
    }
}

fun form(config: Form.() -> Unit) = Form().apply(config)

fun Fragment.form(config: Form.() -> Unit): Form {
    val form = Form().apply(config)
    viewLifecycleOwnerLiveData.observe(this) { lifecycleOwner ->
        lifecycleOwner.lifecycleScope.launchWhenCreated {
            form.apply {
                initViews(requireView())
                validate()
            }
        }
    }
    return form
}