package com.grafsemmel.valium.form

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.grafsemmel.valium.ValidationState
import com.grafsemmel.valium.isValid

open class Form {
    val validation: MediatorLiveData<Boolean> = object : MediatorLiveData<Boolean>() {
        override fun onActive() {
            super.onActive()
            checkAllFields()
        }

    }
    private val inputFields = mutableListOf<Field>()
    private val inputFieldsObserver: Observer<ValidationState> = Observer { checkAllFields() }

    fun checkAllFields() {
        // the form is not valid if one field inside the form is not valid
        validation.value =
            inputFields.none { field -> field.validationState.value is ValidationState.NotValid }
    }

    fun addField(field: Field) {
        inputFields.add(field)
        validation.addSource(field.validationState, inputFieldsObserver)
    }
}

object FormBindings {

    @JvmStatic
    @BindingAdapter("app:enabled")
    fun setEnabled(button: Button, validationState: ValidationState?) {
        validationState?.let { button.isEnabled = it.isValid() }
    }

}