package graf.semmel.valium.helper

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.grafsemmel.valium.R
import org.hamcrest.CoreMatchers.not

class ContactDetailsFragmentRobot : BaseRobot() {

    private val submitButton get() = Espresso.onView(withId(R.id.btn_submit))

    fun setFirstName() {
        fillEditText(R.id.et_first_name, "First")
        sleep(1f)
    }

    fun setFirstNameToShort() {
        fillEditText(R.id.et_first_name, "F")
        sleep(1f)
    }

    fun setLastName() {
        fillEditText(R.id.et_last_name, "Last")
        sleep(1f)
    }

    fun setEmail() {
        fillEditText(R.id.et_email, "first.last@gmail.com")
        sleep(1f)
    }

    fun setWrongEmail() {
        fillEditText(R.id.et_email, "first.last")
        sleep(1f)
    }

    fun submitButtonShouldBeDisabled() {
        submitButton.check(matches(not(isEnabled())))
        sleep(1f)
    }

    fun submitButtonShouldBeEnabled() {
        submitButton.check(matches(isEnabled()))
        sleep(1f)
    }
}

fun onContactDetailsForm(action: ContactDetailsFragmentRobot.() -> Unit) = ContactDetailsFragmentRobot().apply(action)