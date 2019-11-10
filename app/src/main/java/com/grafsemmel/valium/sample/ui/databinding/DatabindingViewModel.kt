package com.grafsemmel.valium.sample.ui.databinding

import androidx.lifecycle.ViewModel
import com.grafsemmel.valium.FieldValidatorBuilder
import com.grafsemmel.valium.form.InputField
import com.grafsemmel.valium.sample.ui.simple.SimpleFragment

class DatabindingViewModel : ViewModel() {

    val minLengthValidator = InputField(FieldValidatorBuilder().min(3).build())
    val chainedValidator = InputField(FieldValidatorBuilder().notBlank().max(10).build())
    val customValidator = InputField(SimpleFragment.StartsWithHttpFieldValidator())

}