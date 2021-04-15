package graf.semmel.valium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grafsemmel.valium.R
import graf.semmel.valium.form.form

class ContactDetailsFragment : Fragment() {

    val form = form {
        inputField(R.id.et_first_name) {
            rules {
                notBlank()
                min(3)
                max(100)
            }
        }
        inputField(R.id.et_last_name) {
            rules {
                notBlank()
                min(3)
                max(100)
            }
        }
        inputField(R.id.et_email) {
            rules {
                notBlank()
                isEmail()
            }
        }
        submitButton(R.id.btn_submit)
        onValidationChange { /* isValid -> viewModel.onFormValidationChange(isValid) */ }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_details, container, false)
}