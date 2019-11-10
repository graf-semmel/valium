package com.grafsemmel.valium.form

import androidx.lifecycle.MediatorLiveData
import com.grafsemmel.valium.ValidationState

open class Field(var isOptional: Boolean = false) {
    val validationState: MediatorLiveData<ValidationState> = MediatorLiveData()
}

