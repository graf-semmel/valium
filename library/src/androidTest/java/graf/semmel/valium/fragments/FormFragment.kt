package graf.semmel.valium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grafsemmel.valium.R
import graf.semmel.valium.form.form

class FormFragment : Fragment() {

    val form = form {
        inputField(R.id.til_1) {
            rules {
                notBlank()
                min(3)
            }
        }
        inputField(R.id.et_2) {
            rules {
                notBlank()
                min(3)
            }
        }
        inputField(R.id.et_3) {
            required = false
            rules {
                notBlank()
                min(3)
            }
        }
        submitButton(R.id.btn_submit)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_form, container, false)
}