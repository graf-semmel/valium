package com.grafsemmel.valium.form

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.grafsemmel.valium.FieldValidator
import com.grafsemmel.valium.ValidationState
import com.grafsemmel.valium.isValid

class InputField(validator: FieldValidator? = null, isOptional: Boolean = false) :
    Field(isOptional) {
    val text: MutableLiveData<String> = MutableLiveData()
    val error = MediatorLiveData<ValidationState>().apply {
        addSource(validationState) { validationState: ValidationState? ->
            val string = text.value
            if (showErrorOnInputChange && string?.length ?: 0 > showErrorOnInputChangeThreshold) {
                this.value = validationState
            } else {
                this.value = null
            }
        }
    }
    var showErrorOnInputChange = true
    var showErrorOnInputChangeThreshold = 0
    var validator = validator
        set(value) {
            validationState.removeSource(text)
            validationState.addSource(text) {
                validate()
            }
            field = value
            validate()
        }
    val isValid = validationState.value?.isValid() == true

    init {
        validator?.let {
            validationState.addSource(text) { validate() }
            validate()
        }
    }

    fun validate(): Boolean {
        val string = text.value.orEmpty()
        val currentValidator = validator

        val state = when {
            isOptional -> when {
                string.isBlank() -> ValidationState.Valid
                else -> currentValidator?.validate(string) ?: ValidationState.Valid
            }
            else -> when {
                currentValidator != null -> currentValidator.validate(string)
                else -> ValidationState.from(string.isNotBlank())
            }
        }

        validationState.value = state

        return state.isValid()
    }

    fun showError() {
        error.value = validationState.value
    }
}

object FieldBindings {

    @JvmStatic
    @BindingAdapter("app:error")
    fun setError(editText: EditText, validationState: ValidationState?) {
        when (validationState) {
            is ValidationState.NotValid -> {
                if (validationState.errorStringRes > 0) {
                    editText.error = editText.resources.getString(
                        validationState.errorStringRes,
                        *validationState.placeholders
                    )
                }
            }
            else -> editText.error = null
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(editText: EditText, text: String?) {
        text?.let {
            if (it != editText.text.toString()) {
                editText.setText(it)
            }
        }
    }
}

