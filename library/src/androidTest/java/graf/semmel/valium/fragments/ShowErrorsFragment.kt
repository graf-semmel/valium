package graf.semmel.valium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grafsemmel.valium.R
import graf.semmel.valium.form.form

class ShowErrorsFragment : Fragment() {

    val form1 = form {
        inputField(R.id.et_1_show_errors) {
            rules {
                notBlank()
                min(3)
            }
        }
        showErrors {
            onFirstEdit = false
            onSubsequentEdit = true
            onLeaveField = true
        }
    }
    val form2 = form {
        inputField(R.id.et_2_show_errors) {
            rules {
                notBlank()
                min(3)
            }
        }
        showErrors {
            onFirstEdit = false
            onSubsequentEdit = true
            onLeaveField = true
        }
    }
    val form3 = form {
        inputField(R.id.et_3_show_errors) {
            rules {
                notBlank()
                min(3)
            }
        }
        showErrors {
            onFirstEdit = false
            onSubsequentEdit = false
            onLeaveField = true
        }
    }
    val form4 = form {
        inputField(R.id.et_4_show_errors) {
            rules {
                notBlank()
                min(3)
            }
        }
        showErrors {
            onFirstEdit = false
            onSubsequentEdit = false
            onLeaveField = false
        }
        submitButton(R.id.btn_submit) {
            disableOnError = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_show_errors, container, false)
}