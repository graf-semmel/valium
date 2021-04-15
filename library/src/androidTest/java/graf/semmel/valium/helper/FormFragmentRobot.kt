package graf.semmel.valium.helper

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.grafsemmel.valium.R
import org.hamcrest.CoreMatchers.not

class FormFragmentRobot : BaseRobot() {

    private val submitButton get() = Espresso.onView(withId(R.id.btn_submit))

    fun setInput1Valid() {
        fillEditText(R.id.et_1, "123")
    }

    fun setInput2Valid() {
        fillEditText(R.id.et_2, "123")
    }

    fun setInput3Valid() {
        fillEditText(R.id.et_3, "123")
    }


    fun setInput2TooShort() {
        fillEditText(R.id.et_2, "12")
    }

    fun setInput3TooShort() {
        fillEditText(R.id.et_3, "12")
    }

    fun submitButtonShouldBeDisabled() {
        submitButton.check(matches(not(isEnabled())))
    }

    fun submitButtonShouldBeEnabled() {
        submitButton.check(matches(isEnabled()))
    }
}

fun formFragmentRobot(action: FormFragmentRobot.() -> Unit) = FormFragmentRobot().apply(action)