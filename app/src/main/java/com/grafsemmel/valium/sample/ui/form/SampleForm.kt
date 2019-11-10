package com.grafsemmel.valium.sample.ui.form

import com.grafsemmel.valium.BaseFieldValidator
import com.grafsemmel.valium.FieldValidatorBuilder
import com.grafsemmel.valium.Validators
import com.grafsemmel.valium.form.Form
import com.grafsemmel.valium.form.InputField
import com.grafsemmel.valium.sample.R

class SampleForm : Form() {
    val nameValidator =
        InputField(FieldValidatorBuilder().min(3).max(32).build()).also { addField(it) }
    val emailValidator = InputField(
        BaseFieldValidator(Validators.EmailValidator(), R.string.validation_error_email)
    ).also { addField(it) }
}