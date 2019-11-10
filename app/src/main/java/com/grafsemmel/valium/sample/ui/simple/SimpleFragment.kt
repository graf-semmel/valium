package com.grafsemmel.valium.sample.ui.simple

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.grafsemmel.valium.*
import com.grafsemmel.valium.sample.R
import kotlinx.android.synthetic.main.fragment_simple.*

class SimpleFragment : Fragment() {

    private val minLengthValidator = Validators.MinLengthValidator(3)
    private val minLengthFieldValidator = BaseFieldValidator(
        minLengthValidator,
        R.string.validation_error_too_short,
        3
    )
    private val minLengthFieldValidatorBuild = FieldValidatorBuilder().min(3).build()
    private val chainedFieldValidator = FieldValidatorChain(
        BaseFieldValidator(Validators.NotBlankValidator(), R.string.validation_error_is_blank),
        BaseFieldValidator(
            Validators.MaxLengthValidator(10),
            R.string.validation_error_too_long,
            10
        )
    )
    private val chainedFieldValidatorBuild = FieldValidatorBuilder().notBlank().max(10).build()
    private val customFieldValidator = StartsWithHttpFieldValidator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_simple, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        minLengthFieldValidator.bindTo(valium_et_min_length_1)
        valium_et_min_length_2.addTextChangedListener(
            afterTextChanged = { text: Editable? ->
                valium_et_min_length_1_label.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (minLengthValidator.validate(text.toString())) R.color.textColorLabel else R.color.red
                    )
                )
            }
        )
        minLengthFieldValidatorBuild.bindTo(valium_et_min_length_3)
        chainedFieldValidator.bindTo(valium_et_chained_1)
        chainedFieldValidatorBuild.bindTo(valium_et_chained_2)
        customFieldValidator.bindTo(valium_et_custom)
    }

    class StartsWithHttpValidator : Validator {
        override fun validate(value: String): Boolean = value.startsWith("http://")
    }

    class StartsWithHttpFieldValidator :
        BaseFieldValidator(StartsWithHttpValidator(), R.string.validation_error_http)
}