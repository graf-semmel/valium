package com.grafsemmel.valium

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

interface FieldValidator {
    fun validate(value: String): ValidationState
    fun getErrorState(): ValidationState
}

fun FieldValidator.bindTo(editText: EditText) {
    editText.apply {
        addTextChangedListener(afterTextChanged = { text: Editable? ->
            error = when (val state = this@bindTo.validate(text.toString())) {
                ValidationState.Valid -> null
                is ValidationState.NotValid -> context.getString(
                    state.errorStringRes,
                    *state.placeholders
                )
            }
        })
    }
}